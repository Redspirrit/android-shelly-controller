package de.shelly_controller.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createShelly)
        db?.execSQL(createColorMix)
        db?.execSQL(createShellyActions)
        db?.execSQL(createShellyActionsShelly)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${Contract.ShellyContract.TABLE_NAME}")
        db?.execSQL("DROP TABLE IF EXISTS ${Contract.ShellyActionContract.TABLE_NAME}")
        db?.execSQL("DROP TABLE IF EXISTS ${Contract.ColorMixContract.TABLE_NAME}")
        db?.execSQL("DROP TABLE IF EXISTS ${Contract.ShellyActionShellyContract.TABLE_NAME}")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    private val createShelly =
        "CREATE TABLE ${Contract.ShellyContract.TABLE_NAME} (" +
                "${Contract.ShellyContract.COLUMN_IP} TEXT PRIMARY KEY, " +
                "${Contract.ShellyContract.COLUMN_NAME} TEXT)"

    private val createColorMix =
        "CREATE TABLE ${Contract.ColorMixContract.TABLE_NAME} (" +
                "${Contract.ColorMixContract.COLUMN_HASH} TEXT PRIMARY KEY, " +
                "${Contract.ColorMixContract.COLUMN_RED} INTEGER, " +
                "${Contract.ColorMixContract.COLUMN_GREEN} INTEGER, " +
                "${Contract.ColorMixContract.COLUMN_BLUE} INTEGER, " +
                "${Contract.ColorMixContract.COLUMN_WHITE} INTEGER, " +
                "${Contract.ColorMixContract.COLUMN_GAIN} INTEGER)"

    private val createShellyActions =
        "CREATE TABLE ${Contract.ShellyActionContract.TABLE_NAME} (" +
                "${Contract.ShellyActionContract.COLUMN_HASH} TEXT PRIMARY KEY, " +
                "${Contract.ShellyActionContract.COLUMN_IS_ENABLED} INTEGER, " +
                "${Contract.ShellyActionContract.COLUMN_IS_LIGHT} INTEGER, " +
                "${Contract.ShellyActionContract.COLUMN_COLOR_ID} INTEGER, " +
                "FOREIGN KEY(${Contract.ShellyActionContract.COLUMN_COLOR_ID}) REFERENCES ${Contract.ColorMixContract.TABLE_NAME}(${BaseColumns._ID}))"

    private val createShellyActionsShelly =
        "CREATE TABLE ${Contract.ShellyActionShellyContract.TABLE_NAME} (" +
                "${Contract.ShellyActionShellyContract.COLUMN_SHELLY_ACTION_ID} INTEGER, " +
                "${Contract.ShellyActionShellyContract.COLUMN_SHELLY_ID} INTEGER, " +
                "FOREIGN KEY(${Contract.ShellyActionShellyContract.COLUMN_SHELLY_ID}) REFERENCES ${Contract.ShellyContract.TABLE_NAME}(${BaseColumns._ID}), " +
                "FOREIGN KEY(${Contract.ShellyActionShellyContract.COLUMN_SHELLY_ACTION_ID}) REFERENCES ${Contract.ShellyActionContract.TABLE_NAME}(${BaseColumns._ID}))"

    companion object {
        const val DATABASE_NAME = "ShellyController.db"
        const val DATABASE_VERSION = 1
    }
}