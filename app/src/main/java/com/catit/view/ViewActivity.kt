package com.catit.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.catit.R
import com.catit.sql.SQLHandler
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class ViewActivity : AppCompatActivity() {
  private lateinit var title: TextView
  private lateinit var description: TextView
  private lateinit var date: TextView
  private lateinit var sqlHandler: SQLHandler
  private lateinit var adBanner: AdView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view)

    sqlHandler = SQLHandler(this)

    title = findViewById(R.id.textViewTextTitle)
    description = findViewById(R.id.textViewTextDescription)
    date = findViewById(R.id.textViewTextDate)
    adBanner = findViewById(R.id.adViewBanner)

    adBanner.loadAd(AdRequest.Builder().build())

    setValue(intent.getIntExtra("id", 0))
  }

  private fun setValue(id: Int) {
    val data = sqlHandler.find(id)
    title.text = data?.title ?: ""
    description.text = data?.description ?: ""
    date.text = data?.date ?: ""
  }

  override fun onBackPressed() {
    super.onBackPressed()
    finish()
  }
}