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
) : RecyclerView.Adapter<ModelViewHolder>() {
    var modelArrayList: ArrayList<Model> = ArrayList()
    var sqLiteDatabase: SQLiteDatabase

    //generate constructor
    init {
        this.modelArrayList = modelArrayList
        this.sqLiteDatabase = sqLiteDatabase
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.singledata, null)
        return ModelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val model = modelArrayList[position]
        holder.txttopic.text = model.topic
        holder.txtdescription.text = model.description
        holder.txtdate.text = model.date

        //click on button go to main activity
        holder.edit.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", model.id)
            bundle.putString("topic", model.topic)
            bundle.putString("description", model.description)
            bundle.putString("date", model.date)
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("userdata", bundle)
            context.startActivity(intent)
        }
        //delete row
        holder.delete.setOnClickListener(object : View.OnClickListener {
            var dBmain: DBmain = DBmain(context)
            @SuppressLint("NotifyDataSetChanged")
            override fun onClick(v: View) {
                sqLiteDatabase = dBmain.readableDatabase
                val delele =
                    sqLiteDatabase.delete(DBmain.TABLENAME, "id=" + model.id, null).toLong()
                if (delele != -1L) {
                    Toast.makeText(context, "deleted data successfully", Toast.LENGTH_SHORT).show()
                    modelArrayList.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        })
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