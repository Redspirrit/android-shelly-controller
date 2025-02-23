package de.shelly_controller.model.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import de.shelly_controller.model.ColorMix
import de.shelly_controller.model.Shelly
import de.shelly_controller.model.ShellyAction

class ShellyDao {
    fun insertShelly(db: SQLiteDatabase, shelly: Shelly): Long {
        val values = ContentValues().apply {
            put(Contract.ShellyContract.COLUMN_IP, shelly.ip)
            put(Contract.ShellyContract.COLUMN_NAME, shelly.name)
        }
        return db.insert(Contract.ShellyContract.TABLE_NAME, null, values)
    }

    fun getShellys(db: SQLiteDatabase): List<Shelly> {
        val shellys = mutableListOf<Shelly>()
        val cursor = db.rawQuery("SELECT * FROM ${Contract.ShellyContract.TABLE_NAME}", null)
        while(cursor.moveToNext()) {
            val ip = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ShellyContract.COLUMN_IP))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ShellyContract.COLUMN_NAME))
            shellys.add(Shelly(ip, name))
        }
        cursor.close()
        return shellys
    }

    fun getShelly(db: SQLiteDatabase, ip: String): Shelly? {
        val cursor = db.rawQuery("SELECT * FROM ${Contract.ShellyContract.TABLE_NAME} WHERE ${Contract.ShellyContract.COLUMN_IP} = ${ip}", null)
        val ipAddress: String
        val name: String
        var shelly: Shelly? = null
        if (cursor.moveToFirst()) {
            ipAddress = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ShellyContract.COLUMN_IP))
            name = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ShellyContract.COLUMN_NAME))
            shelly = Shelly(ipAddress, name)
        }
        cursor.close()
        return shelly
    }

    private fun insertColorMix(db: SQLiteDatabase, colorMix: ColorMix) {
        val values = ContentValues().apply {
            put(Contract.ColorMixContract.COLUMN_HASH, colorMix.hash)
            put(Contract.ColorMixContract.COLUMN_RED, colorMix.red)
            put(Contract.ColorMixContract.COLUMN_GREEN, colorMix.green)
            put(Contract.ColorMixContract.COLUMN_BLUE, colorMix.blue)
            put(Contract.ColorMixContract.COLUMN_WHITE, colorMix.white)
            put(Contract.ColorMixContract.COLUMN_GAIN, colorMix.gain)
        }
        db.insert(Contract.ColorMixContract.TABLE_NAME, null, values)
    }

    private fun getColorMix(db: SQLiteDatabase, colorMixHash: String): ColorMix? {
        val cursor = db.rawQuery("SELECT * FROM ${Contract.ColorMixContract.TABLE_NAME} WHERE ${Contract.ColorMixContract.COLUMN_HASH} = $colorMixHash", null)
        var color: ColorMix? = null
        if (cursor.moveToFirst()) {
            val red = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ColorMixContract.COLUMN_RED))
            val green = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ColorMixContract.COLUMN_GREEN))
            val blue = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ColorMixContract.COLUMN_BLUE))
            val white = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ColorMixContract.COLUMN_WHITE))
            val gain = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ColorMixContract.COLUMN_GAIN))

            color = ColorMix(red, green, blue, white, gain)
        }
        cursor.close()
        return color
    }

    fun insertShellyAction(db: SQLiteDatabase, shellyAction: ShellyAction): String {
        if (!shellyActionExists(db, shellyAction.hash)) {
            return ""
        } else {
            val colorHash = shellyAction.color.hash
            if (getColorMix(db, colorHash) == null) insertColorMix(db, shellyAction.color)

            val values = ContentValues().apply {
                put(Contract.ShellyActionContract.COLUMN_HASH, shellyAction.hash)
                put(Contract.ShellyActionContract.COLUMN_IS_ENABLED, if (shellyAction.isEnabled) 1 else 0)
                put(Contract.ShellyActionContract.COLUMN_IS_LIGHT, if (shellyAction.isLight) 1 else 0)
                put(Contract.ShellyActionContract.COLUMN_COLOR_ID, colorHash)
            }
            db.insert(Contract.ShellyActionContract.TABLE_NAME, null, values)

            for (shelly in shellyAction.shelly) {
                if (getShelly(db, shelly.ip) == null) {
                    insertShelly(db, shelly)
                }
                val linkValues = ContentValues().apply {
                    put(Contract.ShellyActionShellyContract.COLUMN_SHELLY_ID, shelly.ip)
                    put(Contract.ShellyActionShellyContract.COLUMN_SHELLY_ACTION_ID, shellyAction.hash)
                }
                db.insert(Contract.ShellyActionShellyContract.TABLE_NAME, null, linkValues)
            }
            return shellyAction.hash
        }
    }

    private fun shellyActionExists(db: SQLiteDatabase, hash: String): Boolean {
        db.rawQuery("SELECT * FROM ${Contract.ShellyActionContract.TABLE_NAME} WHERE ${Contract.ShellyActionContract.COLUMN_HASH} = $hash", null).use { cursor ->
            return cursor.moveToFirst()
        }
    }

    fun getShellyActions(db: SQLiteDatabase): List<ShellyAction> {
        val shellyActions = mutableListOf<ShellyAction>()
        val cursor = db.rawQuery("SELECT * FROM ${Contract.ShellyActionContract.TABLE_NAME}", null)

        while (cursor.moveToNext()) {
            val isEnabled = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ShellyActionContract.COLUMN_IS_ENABLED)) == 1
            val isLight = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ShellyActionContract.COLUMN_IS_LIGHT)) == 1
            val colorMixHash = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ShellyActionContract.COLUMN_COLOR_ID))
            val color = getColorMix(db, colorMixHash)

            val shellyCursor = db.rawQuery(
                "SELECT s.* FROM ${Contract.ShellyContract.TABLE_NAME} s " +
                        "JOIN ${Contract.ShellyActionShellyContract.TABLE_NAME} sas " +
                        "ON s.${Contract.ShellyContract.COLUMN_IP} = sas.${Contract.ShellyActionShellyContract.COLUMN_SHELLY_ID}" +
                        "WHERE sas.${Contract.ShellyActionShellyContract.COLUMN_SHELLY_ACTION_ID} = ${cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))}", null
            )

            val shellys = mutableListOf<Shelly>()
            while (shellyCursor.moveToNext()) {
                shellys.add(
                    Shelly(
                        ip = shellyCursor.getString(shellyCursor.getColumnIndexOrThrow(Contract.ShellyContract.COLUMN_IP)),
                        name = shellyCursor.getString(shellyCursor.getColumnIndexOrThrow(Contract.ShellyContract.COLUMN_NAME))
                    )
                )
            }
            shellyCursor.close()
            shellyActions.add(ShellyAction(shellys, isEnabled, isLight, color!!))
        }
        cursor.close()

        return shellyActions
    }
}