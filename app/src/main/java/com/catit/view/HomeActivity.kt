package com.catit.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.catit.R
import com.catit.adapter.CardAdapter
import com.catit.model.Card
import com.catit.sql.SQLHandler
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {
  private lateinit var cardAdapter: CardAdapter
  private lateinit var recyclerView: RecyclerView
  private lateinit var addButton: FloatingActionButton
  private lateinit var sqlHandler: SQLHandler
  private lateinit var adBanner: AdView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)

    cardAdapter = CardAdapter(this)
    sqlHandler = SQLHandler(this)

    addButton = findViewById(R.id.floatingActionButtonAdd)
    adBanner = findViewById(R.id.adViewBanner)

    adBanner.loadAd(AdRequest.Builder().build())

    recyclerView = findViewById<RecyclerView?>(R.id.RecyclerViewContainer).apply {
      layoutManager = LinearLayoutManager(this@HomeActivity)
      adapter = cardAdapter
    }

    addButton.setOnClickListener {
      val intent = Intent(this, FormActivity::class.java)
      intent.putExtra("isAdd", true)
      startActivity(intent)
    }

    loadData()
  }

  override fun onResume() {
    super.onResume()
    loadData()
  }

  private fun loadData() {
    val data = sqlHandler.read()
    cardAdapter.clear()
    for (i in data.indices) {
      cardAdapter.addItem(Card(data[i].id, data[i].title, data[i].description, data[i].date))
    }
  }
}