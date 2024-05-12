package com.example.taskmanager

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var dBmain: DBmain? = null
    private var sqLiteDatabase: SQLiteDatabase? = null
    private var topic: EditText? = null
    private var description: EditText? = null
    private var date: EditText? = null
    private var submit: Button? = null
    private var display: Button? = null
    private var edit: Button? = null
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dBmain = DBmain(this)
        //create method
        findid()
        insertData()
        editData()

        date!!.setOnClickListener { showDatePickerDialog() }
    }

    private fun editData() {
        if (intent.getBundleExtra("userdata") != null) {
            val bundle = intent.getBundleExtra("userdata")
            id = bundle!!.getInt("id")
            topic!!.setText(bundle.getString("topic"))
            description!!.setText(bundle.getString("description"))
            date!!.setText(bundle.getString("date"))
            edit!!.visibility = View.VISIBLE
            submit!!.visibility = View.GONE
        }
    }

    private fun insertData() {
        submit!!.setOnClickListener {
            val cv = ContentValues()
            cv.put("topic", topic!!.text.toString())
            cv.put("description", description!!.text.toString())
            cv.put("date", date!!.text.toString())

            sqLiteDatabase?.let {
                it.insert(DBmain.TABLENAME, null, cv)
                Toast.makeText(this@MainActivity, "successfully inserted data", Toast.LENGTH_SHORT)
                    .show()
                //clear when click on submit
                topic?.setText("")
                description?.setText("")
                date?.setText("")
            }
        }
        //when click on display button open display data activity
        display!!.setOnClickListener {
            val intent = Intent(this@MainActivity, DisplayData::class.java)
            startActivity(intent)
        }
        //storing edited data
        edit!!.setOnClickListener {
            val cv = ContentValues()
            cv.put("topic", topic!!.text.toString())
            cv.put("description", description!!.text.toString())
            cv.put("date", date!!.text.toString())

            sqLiteDatabase?.let {
                val recedit = it.update(DBmain.TABLENAME, cv, "id=$id", null).toLong()
                if (recedit != -1L) {
                    Toast.makeText(this@MainActivity, "Data updated successfully", Toast.LENGTH_SHORT)
                        .show()
                    submit!!.visibility = View.VISIBLE
                    edit!!.visibility = View.GONE
                } else {
                    Toast.makeText(this@MainActivity, "something wrong try again", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun findid() {
        topic = findViewById<View>(R.id.edit_topic) as EditText
        description = findViewById<View>(R.id.edit_description) as EditText
        date = findViewById<View>(R.id.edit_date) as EditText
        submit = findViewById<View>(R.id.submit_btn) as Button
        display = findViewById<View>(R.id.display_btn) as Button
        edit = findViewById<View>(R.id.edit_btn) as Button
    }

    @SuppressLint("SetTextI18n")
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
                date!!.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
            }, mYear, mMonth, mDay
        )

        datePickerDialog.show()
    }
}
