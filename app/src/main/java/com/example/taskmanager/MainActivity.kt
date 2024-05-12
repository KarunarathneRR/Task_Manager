package com.example.taskmanager

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var dBmain: DBmain? = null
    var sqLiteDatabase: SQLiteDatabase? = null
    var topic: EditText? = null
    var description: EditText? = null
    var date: EditText? = null
    var submit: Button? = null
    var display: Button? = null
    var edit: Button? = null
    var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dBmain = DBmain(this)
        //create method
        findid()
        // Load data to edit if received from DisplayData activity
        editData()

        date!!.setOnClickListener { showDatePickerDialog() }

        // Initialize submit and display buttons
        submit!!.setOnClickListener { insertOrUpdateData() }
        display!!.setOnClickListener { navigateToDisplayData() }
    }

    private fun insertOrUpdateData() {
        val cv = ContentValues()
        cv.put("topic", topic!!.text.toString())
        cv.put("description", description!!.text.toString())
        cv.put("date", date!!.text.toString())

        sqLiteDatabase = dBmain?.writableDatabase

        if (id != 0) {
            // Update existing data
            val recEdit = sqLiteDatabase?.update(DBmain.TABLENAME, cv, "id=$id", null)
            if (recEdit != -1) {
                Toast.makeText(this@MainActivity, "Data updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Failed to update data", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Insert new data
            val recInsert = sqLiteDatabase?.insert(DBmain.TABLENAME, null, cv)
            if (recInsert != -1L) {
                Toast.makeText(this@MainActivity, "Data inserted successfully", Toast.LENGTH_SHORT).show()
                // Clear input fields after successful insertion
                clearInputFields()
            } else {
                Toast.makeText(this@MainActivity, "Failed to insert data", Toast.LENGTH_SHORT).show()
            }
        }
        Log.d("InsertOrUpdate", "ID: $id")
    }

    private fun editData() {
        val bundle = intent.getBundleExtra("userdata")
        if (bundle != null) {
            id = bundle.getInt("id")
            topic!!.setText(bundle.getString("topic"))
            description!!.setText(bundle.getString("description"))
            date!!.setText(bundle.getString("date"))
            // Change visibility of buttons based on edit mode
            edit!!.visibility = View.VISIBLE
            submit!!.visibility = View.GONE
        }
    }

    private fun navigateToDisplayData() {
        val intent = Intent(this@MainActivity, DisplayData::class.java)
        startActivity(intent)
    }

    private fun findid() {
        topic = findViewById<View>(R.id.edit_topic) as EditText
        description = findViewById<View>(R.id.edit_description) as EditText
        date = findViewById<View>(R.id.edit_date) as EditText
        submit = findViewById<View>(R.id.submit_btn) as Button
        display = findViewById<View>(R.id.display_btn) as Button
        edit = findViewById<View>(R.id.edit_btn) as Button // Add this line to initialize the edit button
    }

    private fun showDatePickerDialog() {
        // Get current date
        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mMonth = c[Calendar.MONTH]
        val mDay = c[Calendar.DAY_OF_MONTH]

        // Create a date picker dialog and show it
        val datePickerDialog = DatePickerDialog(
            this@MainActivity,
            { view, year, monthOfYear, dayOfMonth -> // Set selected date to the date EditText field
                date!!.setText("$dayOfMonth/${monthOfYear + 1}/$year")
            }, mYear, mMonth, mDay
        )

        datePickerDialog.show()
    }

    private fun clearInputFields() {
        topic?.setText("")
        description?.setText("")
        date?.setText("")
    }
}
