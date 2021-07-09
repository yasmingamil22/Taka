package com.example.taka
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*
class SqliteDatabase internal constructor(context: Context?) :
            SQLiteOpenHelper(
                    context,
                    DATABASE_NAME,
                    null,
                    DATABASE_VERSION
            ) {
        override fun onCreate(db: SQLiteDatabase) {
            val createContactTable = ("CREATE TABLE "
                    + TABLE_TASKS + "(" + COLUMN_ID
                    + " INTEGER PRIMARY KEY,"
                    + COLUMN_NAME + " TEXT"+ ")")
            db.execSQL(createContactTable)
        }
        override fun onUpgrade(
                db: SQLiteDatabase,
                oldVersion: Int,
                newVersion: Int
        ) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
            onCreate(db)
        }
        fun listTasks(): ArrayList<Task_Content> {
            val sql = "select * from $TABLE_TASKS"
            val db = this.readableDatabase
            val storeTasks =
                    ArrayList<Task_Content>()
            val cursor = db.rawQuery(sql, null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(0).toInt()
                    val name = cursor.getString(1)
                    storeTasks.add(Task_Content(id, name))
                }
                while (cursor.moveToNext())
            }
            cursor.close()
            return storeTasks
        }
        fun addTasks(tasks: Task_Content) {
            val values = ContentValues()
            values.put(COLUMN_NAME, tasks.content)
            val db = this.writableDatabase
            db.insert(TABLE_TASKS, null, values)
        }
        fun updateTasks(tasks: Task_Content) {
            val values = ContentValues()
            values.put(COLUMN_NAME, tasks.content)
            val db = this.writableDatabase
            db.update(
                    TABLE_TASKS,
                    values,
                    "$COLUMN_ID = ?",
                    arrayOf(tasks.id.toString())
            )
        }
        fun deleteTask(id: Int) {
            val db = this.writableDatabase
            db.delete(
                    TABLE_TASKS,
                    "$COLUMN_ID = ?",
                    arrayOf(id.toString())
            )
        }
        companion object {
            private const val DATABASE_VERSION = 5
            private const val DATABASE_NAME = "Tasks"
            private const val TABLE_TASKS = "TASK"
            private const val COLUMN_ID = "_id"
            private const val COLUMN_NAME = "taskContent"
        }
}