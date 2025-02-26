package de.shelly_controller.model

import android.content.Context
import android.util.Log
import de.shelly_controller.model.database.DatabaseHelper
import de.shelly_controller.model.database.ShellyDao

class ShellyRepository(private val dbHelper: DatabaseHelper, private val shellyDao: ShellyDao) {

    fun addShelly(shelly: Shelly) {
        val db = dbHelper.writableDatabase
        shellyDao.insertShelly(db, shelly)
    }

    fun getShellys(): List<Shelly> {
        val db = dbHelper.readableDatabase
        return shellyDao.getShellys(db)
    }

    fun getShelly(ip: String): Shelly? {
        val db = dbHelper.readableDatabase
        return shellyDao.getShelly(db, ip)
    }

    fun getHighesColorId(): Int {
        val db = dbHelper.readableDatabase
        return shellyDao.getHighesColorId(db)
    }

    fun getHighestActionId(): Int {
        val db = dbHelper.readableDatabase
        return shellyDao.getHighestActionId(db)
    }

    fun insertShellyAction(shellyAction: ShellyAction) {
        val db = dbHelper.writableDatabase
        val id = shellyDao.insertShellyAction(db, shellyAction)
        Log.d("db", id.toString())
    }

    fun getShellyActions(): List<ShellyAction> {
        val db = dbHelper.readableDatabase
        return shellyDao.getShellyActions(db)
    }
}