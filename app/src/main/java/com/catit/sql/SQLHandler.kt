package com.catit.sql

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.text.SimpleDateFormat
import com.catit.model.Todo

class SQLHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

  override fun onCreate(db: SQLiteDatabase?) {
    val createTable = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY,$TITLE TEXT,$DESCRIPTION TEXT, $DATE TEXT);"
    db!!.execSQL(createTable)
  }

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    onCreate(db)
  }

  fun create(todo: Todo): Boolean {
    val db = this.writableDatabase
    val values = ContentValues()
    values.put(TITLE, todo.title)
    values.put(DESCRIPTION, todo.description)
    values.put(DATE, todo.date)
    val success = db.insert(TABLE_NAME, null, values)
    db.close()
    return (Integer.parseInt("$success") != -1)
  }

  fun read(): ArrayList<Todo> {
    val todos = ArrayList<Todo>()
    val db = this.readableDatabase
    val selectQuery = "SELECT * FROM $TABLE_NAME"
    val cursor = db.rawQuery(selectQuery, null)
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        do {
          try {
            todos.add(
              Todo(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))),
                cursor.getString(cursor.getColumnIndex(TITLE)),
                cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(DATE))
              )
            )
          } catch (e: Exception) {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val date = dateFormat.format(System.currentTimeMillis())
            todos.add(
              Todo(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))),
                cursor.getString(cursor.getColumnIndex(TITLE)),
                cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
                date
              )
            )
          }
        } while (cursor.moveToNext())
      }
    }
    cursor.close()
    db.close()
    return todos
  }

  fun find(id: Int): Todo? {
    val db = this.readableDatabase
    val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $id"
    val cursor = db.rawQuery(selectQuery, null)
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        try {
          return Todo(
            Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))),
            cursor.getString(cursor.getColumnIndex(TITLE)),
            cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
            cursor.getString(cursor.getColumnIndex(DATE))
          )
        } catch (e: Exception) {
          val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
          val date = dateFormat.format(System.currentTimeMillis())
          return Todo(
            Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))),
            cursor.getString(cursor.getColumnIndex(TITLE)),
            cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
            date
          )
        }
      }
    }
    cursor.close()
    db.close()
    return null
  }

  fun update(todo: Todo): Boolean {
    val db = this.writableDatabase
    val values = ContentValues()
    values.put(TITLE, todo.title)
    values.put(DESCRIPTION, todo.description)
    values.put(DATE, todo.date)
    val success = db.update(TABLE_NAME, values, "$ID = ?", arrayOf(todo.id.toString()))
    db.close()
    return (Integer.parseInt("$success") != -1)
  }

  fun delete(id: Int): Boolean {
    val db = this.writableDatabase
    val success = db.delete(TABLE_NAME, "$ID = ?", arrayOf(id.toString()))
    db.close()
    return (Integer.parseInt("$success") != -1)
  }

  companion object {
    private const val DB_VERSION = 1
    private const val DB_NAME = "CatIt"
    private const val TABLE_NAME = "ToDo"
    private const val ID = "Id"
    private const val TITLE = "Title"
    private const val DESCRIPTION = "Description"
    private const val DATE = "Date"
  }
}