package com.example.taskmanager

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBmain(context: Context?) : SQLiteOpenHelper(context, DBNAME, null, VER) {
    private var query: String? = null
    override fun onCreate(db: SQLiteDatabase) {
        query =
            "create table $TABLENAME(id integer primary key, topic text, description text, date date)"
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        query = "drop table if exists $TABLENAME"
        db.execSQL(query)
        onCreate(db)
    }

    companion object {
        const val DBNAME: String = "student.db"
        const val TABLENAME: String = "course"
        const val VER: Int = 1
    }
}