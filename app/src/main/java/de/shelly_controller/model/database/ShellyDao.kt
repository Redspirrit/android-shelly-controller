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
        val cursor = db.rawQuery("SELECT * FROM ${Contract.ShellyContract.TABLE_NAME} WHERE ${Contract.ShellyContract.COLUMN_IP} = '$ip'", null)
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

    fun updateShelly(db: SQLiteDatabase, shelly: Shelly): Int {
        val values = ContentValues().apply {
            put(Contract.ShellyContract.COLUMN_IP, shelly.ip)
            put(Contract.ShellyContract.COLUMN_NAME, shelly.name)
        }

        return db.update(
            Contract.ShellyContract.TABLE_NAME,
            values,
            "${Contract.ShellyContract.COLUMN_IP} = ${shelly.ip}",
            null
        )
    }

    private fun insertColorMix(db: SQLiteDatabase, colorMix: ColorMix): Long {
        val values = ContentValues().apply {
            put(Contract.ColorMixContract.COLUMN_HASH, colorMix.hash)
            put(Contract.ColorMixContract.COLUMN_RED, colorMix.red)
            put(Contract.ColorMixContract.COLUMN_GREEN, colorMix.green)
            put(Contract.ColorMixContract.COLUMN_BLUE, colorMix.blue)
            put(Contract.ColorMixContract.COLUMN_WHITE, colorMix.white)
            put(Contract.ColorMixContract.COLUMN_GAIN, colorMix.gain)
        }
        return db.insert(Contract.ColorMixContract.TABLE_NAME, null, values)
    }

    fun getHighesColorId(db: SQLiteDatabase): Int {
        val cursor = db.rawQuery("SELECT MAX(${BaseColumns._ID}) FROM ${Contract.ColorMixContract.TABLE_NAME}", null)
        var id = -1
        while (cursor.moveToNext()) {
            id = cursor.getInt(0)
        }
        cursor.close()
        return id
    }

    private fun getColorMix(db: SQLiteDatabase, colorMixHash: String): ColorMix? {
        val cursor = db.rawQuery("SELECT * FROM ${Contract.ColorMixContract.TABLE_NAME} WHERE ${Contract.ColorMixContract.COLUMN_HASH} = '$colorMixHash'", null)
        var color: ColorMix? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val red = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ColorMixContract.COLUMN_RED))
            val green = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ColorMixContract.COLUMN_GREEN))
            val blue = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ColorMixContract.COLUMN_BLUE))
            val white = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ColorMixContract.COLUMN_WHITE))
            val gain = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ColorMixContract.COLUMN_GAIN))

            color = ColorMix(id, red, green, blue, white, gain)
        }
        cursor.close()
        return color
    }

    private fun updateColorMIx(db: SQLiteDatabase, colorMix: ColorMix): Int {
        val values = ContentValues().apply {
            put(Contract.ColorMixContract.COLUMN_RED, colorMix.red)
            put(Contract.ColorMixContract.COLUMN_GREEN, colorMix.green)
            put(Contract.ColorMixContract.COLUMN_BLUE, colorMix.blue)
            put(Contract.ColorMixContract.COLUMN_WHITE, colorMix.white)
            put(Contract.ColorMixContract.COLUMN_GAIN, colorMix.gain)
        }

        return db.update(
            Contract.ColorMixContract.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ${colorMix.id}",
            null
        )
    }

    private fun deleteColorMix(db: SQLiteDatabase, colorMix: ColorMix): Int {
        return db.delete(
            Contract.ColorMixContract.TABLE_NAME,
            "${BaseColumns._ID} = ${colorMix.id}",
            null
        )
    }

    fun getHighestActionId(db: SQLiteDatabase): Int {
        val cursor = db.rawQuery("SELECT MAX(${BaseColumns._ID}) FROM ${Contract.ShellyActionContract.TABLE_NAME}", null)
        var id = -1
        while (cursor.moveToNext()) {
            id = cursor.getInt(0)
        }
        cursor.close()
        return id
    }

    fun insertShellyAction(db: SQLiteDatabase, shellyAction: ShellyAction): Long {
        if (shellyActionExists(db, shellyAction.hash)) {
            return -1
        } else {
            if (getColorMix(db, shellyAction.color.hash) == null) insertColorMix(db, shellyAction.color)

            val values = ContentValues().apply {
                put(Contract.ShellyActionContract.COLUMN_HASH, shellyAction.hash)
                put(Contract.ShellyActionContract.COLUMN_IS_ENABLED, if (shellyAction.isEnabled) 1 else 0)
                put(Contract.ShellyActionContract.COLUMN_IS_LIGHT, if (shellyAction.isLight) 1 else 0)
                put(Contract.ShellyActionContract.COLUMN_COLOR_ID, shellyAction.color.id)
            }

            for (shelly in shellyAction.shelly) {
                if (getShelly(db, shelly.ip) == null) {
                    insertShelly(db, shelly)
                }
                val linkValues = ContentValues().apply {
                    put(Contract.ShellyActionShellyContract.COLUMN_SHELLY_ID, shelly.ip)
                    put(Contract.ShellyActionShellyContract.COLUMN_SHELLY_ACTION_ID, shellyAction.id)
                }
                db.insert(Contract.ShellyActionShellyContract.TABLE_NAME, null, linkValues)
            }

            return db.insert(Contract.ShellyActionContract.TABLE_NAME, null, values)
        }
    }

    private fun shellyActionExists(db: SQLiteDatabase, hash: String): Boolean {
        db.rawQuery("SELECT * FROM ${Contract.ShellyActionContract.TABLE_NAME} WHERE ${Contract.ShellyActionContract.COLUMN_HASH} = '$hash'", null).use { cursor ->
            return cursor.moveToFirst()
        }
    }

    fun getShellyActions(db: SQLiteDatabase): List<ShellyAction> {
        val shellyActions = mutableListOf<ShellyAction>()
        val cursor = db.rawQuery("SELECT * FROM ${Contract.ShellyActionContract.TABLE_NAME}", null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
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
            shellyActions.add(ShellyAction(id, shellys, isEnabled, isLight, color!!))
        }
        cursor.close()

        return shellyActions
    }

    fun updateShellyAction(db: SQLiteDatabase, shellyAction: ShellyAction): Int {
        val values = ContentValues().apply {
            put(Contract.ShellyActionContract.COLUMN_HASH, shellyAction.hash)
            put(Contract.ShellyActionContract.COLUMN_IS_ENABLED, if (shellyAction.isEnabled) 1 else 0)
            put(Contract.ShellyActionContract.COLUMN_IS_LIGHT, if (shellyAction.isLight) 1 else 0)
            put(Contract.ShellyActionContract.COLUMN_COLOR_ID, shellyAction.color.hash)
        }

        return db.update(
            Contract.ShellyActionContract.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ${shellyAction.id}",
            null
        )
    }

    fun deleteShellyAction(db: SQLiteDatabase, shellyAction: ShellyAction): Int {
        val deleteShellyActionShelly = db.delete(
            Contract.ShellyActionShellyContract.TABLE_NAME,
            "${Contract.ShellyActionShellyContract.COLUMN_SHELLY_ACTION_ID} = ${shellyAction.hash}",
            null
        )
        return if (deleteShellyActionShelly > 0) {
            db.delete(
                Contract.ShellyActionContract.TABLE_NAME,
                "${Contract.ShellyActionContract.COLUMN_HASH} = ${shellyAction.hash}",
                null
            )
        } else deleteShellyActionShelly
    }
}