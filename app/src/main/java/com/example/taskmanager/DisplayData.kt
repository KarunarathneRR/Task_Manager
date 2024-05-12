package com.example.taskmanager

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DisplayData : AppCompatActivity() {
    private var dBmain: DBmain? = null
    private var sqLiteDatabase: SQLiteDatabase? = null
    private var recyclerView: RecyclerView? = null
    private var myAdapter: MyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_data)
        dBmain = DBmain(this)

        // Create method
        findid()
        displayData()
        recyclerView?.layoutManager = LinearLayoutManager(this)
    }

    private fun displayData() {
        sqLiteDatabase = dBmain?.readableDatabase
        val cursor = sqLiteDatabase?.rawQuery("select * from " + DBmain.TABLENAME, null)
        val modelArrayList = ArrayList<Model>()
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(0)
                val topic = it.getString(1)
                val description = it.getString(2)
                val date = it.getString(3)

                modelArrayList.add(Model(id, topic, description, date))
            }
        }
        cursor?.close()
        myAdapter = sqLiteDatabase?.let { MyAdapter(this, modelArrayList, it) }
        recyclerView?.adapter = myAdapter
    }

    private fun findid() {
        recyclerView = findViewById(R.id.rv)
    }
}
