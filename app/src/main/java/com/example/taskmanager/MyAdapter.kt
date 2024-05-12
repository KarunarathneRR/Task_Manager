package com.example.taskmanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.MyAdapter.ModelViewHolder

class MyAdapter(
    var context: Context,
    modelArrayList: ArrayList<Model>,
    sqLiteDatabase: SQLiteDatabase
) : RecyclerView.Adapter<MyAdapter.ModelViewHolder>() {
    var modelArrayList: ArrayList<Model> = ArrayList()
    var sqLiteDatabase: SQLiteDatabase

    // Generate constructor
    init {
        this.modelArrayList = modelArrayList
        this.sqLiteDatabase = sqLiteDatabase
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.singledata, parent, false) // Inflate layout with parent
        return ModelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        val model = modelArrayList[position]
        holder.txttopic.text = model.topic
        holder.txtdescription.text = model.description
        holder.txtdate.text = model.date

        // Click listener for edit button
        holder.edit.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("id", model.id)
                putString("topic", model.topic)
                putString("description", model.description)
                putString("date", model.date)
            }
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("userdata", bundle)
            }
            context.startActivity(intent)
        }

        // Click listener for delete button
        holder.delete.setOnClickListener {
            val dBmain = DBmain(context)
            val delete = sqLiteDatabase.delete(DBmain.TABLENAME, "id=${model.id}", null)
            if (delete != -1) {
                Toast.makeText(context, "Deleted data successfully", Toast.LENGTH_SHORT).show()
                modelArrayList.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return modelArrayList.size
    }

    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txttopic: TextView = itemView.findViewById<View>(R.id.txttopic) as TextView
        var txtdescription: TextView = itemView.findViewById<View>(R.id.txtdescription) as TextView
        var txtdate: TextView = itemView.findViewById<View>(R.id.txtdate) as TextView
        var edit: Button = itemView.findViewById<View>(R.id.txt_btn_edit) as Button
        var delete: Button = itemView.findViewById<View>(R.id.txt_btn_delete) as Button
    }
}
