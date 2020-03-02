package com.xbrid.freefalldetector.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xbrid.freefalldetector.utils.DetectionEvent

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "FreeFallDB", null, 1) {

    private val tableFreeFall = "freeFallTable"
    private val id = "id"
    private val timestamp = "timestamp"
    private val duration = "duration"

    override fun onCreate(db: SQLiteDatabase) {
        val createDbStructure =
            " CREATE TABLE " + tableFreeFall + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    timestamp + " TEXT," +
                    duration + " TEXT)"
        db.execSQL(createDbStructure)
    }

    override
    fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $tableFreeFall")
        this.onCreate(db)
    }

    fun addEvent(event: DetectionEvent): Long {
        val db: SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(timestamp, event.timeStamp)
        contentValues.put(duration, event.duration)
        val id = db.insert(tableFreeFall, null, contentValues)
        db.close()
        println("Saving Event against id: $id")
        return id
    }

    fun getAllEvents(): ArrayList<DetectionEvent> {
        val events: ArrayList<DetectionEvent> = ArrayList()
        val query = "SELECT * FROM $tableFreeFall"
        val db: SQLiteDatabase = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                events.add(
                    DetectionEvent(
                        cursor.getString(cursor.getColumnIndex(timestamp)),
                        cursor.getString(cursor.getColumnIndex(duration))
                    )
                )
            } while (cursor.moveToNext())
        }
        db.close()
        return events
    }

    fun deleteAllEvents() {
        val db: SQLiteDatabase = this.writableDatabase
        db.delete(tableFreeFall, null, null)
    }

}