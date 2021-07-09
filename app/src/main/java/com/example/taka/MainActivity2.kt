package com.example.taka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity2 : AppCompatActivity() {
    private lateinit var dataBase: SqliteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val taskView: RecyclerView = findViewById(R.id.myList)
        val linearLayoutManager = LinearLayoutManager(this)
        taskView.layoutManager = linearLayoutManager
        taskView.setHasFixedSize(true)
        if (dataBase.listTasks().size > 0) {
            taskView.visibility = View.VISIBLE
            val mAdapter = TaskAdapter(this,dataBase.listTasks())
            taskView.adapter = mAdapter
        }
        else {
            taskView.visibility = View.GONE
            Toast.makeText(
                    this,
                    "There is no contact in the database. Start adding now",
                    Toast.LENGTH_LONG
            ).show()
        }
        val btnAdd: Button = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener { addTaskDialog() }
    }
    private fun addTaskDialog() {
        val inflater = LayoutInflater.from(this)
        val subView = inflater.inflate(R.layout.add_task, null)
        val nameField: EditText = subView.findViewById(R.id.enterTask)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add new TASK")
        builder.setView(subView)
        builder.create()
        builder.setPositiveButton("ADD TASK") { _, _ ->
            val task = nameField.text.toString()
            if (TextUtils.isEmpty(task)) {
                Toast.makeText(
                        this,
                        "Something went wrong. Check your input values",
                        Toast.LENGTH_LONG
                ).show()
            }
            else {
                val newTask = Task_Content(task)
                dataBase.addTasks(newTask)
                finish()
                startActivity(intent)
            }
        }
        builder.setNegativeButton("CANCEL") { _, _ -> Toast.makeText(this, "Task cancelled",
                Toast.LENGTH_LONG).show()}
        builder.show()
    }
    override fun onDestroy() {
        super.onDestroy()
        dataBase.close()
    }
}