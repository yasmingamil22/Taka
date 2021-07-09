package com.example.taka
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.util.*
class TaskAdapter (private val context: Context, listTasks: ArrayList<Task_Content>) :
        RecyclerView.Adapter<TaskViewHolder>(), Filterable {
    private var listTasks: ArrayList<Task_Content>
    private val mArrayList: ArrayList<Task_Content>
    private val mDatabase: SqliteDatabase
    init {
        this.listTasks = listTasks
        this.mArrayList = listTasks
        mDatabase = SqliteDatabase(context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view =
                LayoutInflater.from(parent.context).inflate(R.layout.task_list_layout, parent, false)
        return TaskViewHolder(view)
    }
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tasks = listTasks[position]
        holder.tvName.text = tasks.content
        holder.editTask.setOnClickListener { editTaskDialog(tasks) }
        holder.deleteTask.setOnClickListener {
            mDatabase.deleteTask(tasks.id)
            (context as Activity).finish()
            context.startActivity(context.intent)
        }
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                listTasks = if (charString.isEmpty()) {
                    mArrayList
                }
                else {
                    val filteredList = ArrayList<Task_Content>()
                    for (tasks in mArrayList) {
                        if (tasks.content.toLowerCase().contains(charString)) {
                            filteredList.add(tasks)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = listTasks
                return filterResults
            }
            override fun publishResults(
                    charSequence: CharSequence,
                    filterResults: FilterResults
            )
            {
                listTasks =
                        filterResults.values as ArrayList<Task_Content>
                notifyDataSetChanged()
            }
        }
    }
    override fun getItemCount(): Int {
        return listTasks.size
    }
    private fun editTaskDialog(contacts: Task_Content) {
        val inflater = LayoutInflater.from(context)
        val subView = inflater.inflate(R.layout.add_task, null)
        val nameField: EditText = subView.findViewById(R.id.enterTask)
        nameField.setText(contacts.content)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit task")
        builder.setView(subView)
        builder.create()
        builder.setPositiveButton(
                "EDIT TASK"
        ) { _, _ ->
            val task = nameField.text.toString()
            if (TextUtils.isEmpty(task)) {
                Toast.makeText(
                        context,
                        "Something went wrong. Check your input values",
                        Toast.LENGTH_LONG
                ).show()
            }
            else {
                mDatabase.updateTasks(
                        Task_Content(
                                Objects.requireNonNull<Any>(contacts.id) as Int,
                                task
                        )
                )
                (context as Activity).finish()
                context.startActivity(context.intent)
            }
        }
        builder.setNegativeButton(
                "CANCEL"
        ) { _, _ -> Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show() }
        builder.show()
    }
}
