package com.catit.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.catit.MainActivity
import com.catit.R
import com.catit.model.Todo
import com.catit.sql.SQLHandler
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class FormActivity : AppCompatActivity() {
  private lateinit var title: EditText
  private lateinit var description: EditText
  private lateinit var save: Button
  private lateinit var sqlHandler: SQLHandler
  private lateinit var adBanner: AdView
  private var isAdd = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_form)

    isAdd = intent.getBooleanExtra("isAdd", false)

    sqlHandler = SQLHandler(this)

    title = findViewById(R.id.editTextTextTitle)
    description = findViewById(R.id.editTextTextDescription)
    save = findViewById(R.id.buttonSave)
    adBanner = findViewById(R.id.adViewBanner)

    adBanner.loadAd(AdRequest.Builder().build())

    save.setOnClickListener {
      if (title.text.isEmpty()) {
        Toast.makeText(this, "Title is empty", Toast.LENGTH_SHORT).show()
      } else if (description.text.isEmpty()) {
        Toast.makeText(this, "Description is empty", Toast.LENGTH_SHORT).show()
      } else {
        if (isAdd) {
          sqlHandler.create(Todo(0, title.text.toString(), description.text.toString()))
        } else {
          sqlHandler.update(Todo(intent.getIntExtra("id", 0), title.text.toString(), description.text.toString()))
        }
      }
    }

    if (!isAdd) {
      setValue(intent.getIntExtra("id", 0))
    }
  }

  private fun setValue(id: Int) {
    val data = sqlHandler.find(id)
    title.setText(data?.title ?: "")
    description.setText(data?.description ?: "")
  }

  override fun onBackPressed() {
    super.onBackPressed()
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
  }
}