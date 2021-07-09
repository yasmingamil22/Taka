package com.example.taka
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class TaskViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvName: TextView = itemView.findViewById(R.id.task_content)
    var deleteTask: ImageView = itemView.findViewById(R.id.deleteTask)
    var editTask: ImageView = itemView.findViewById(R.id.editTask)
}