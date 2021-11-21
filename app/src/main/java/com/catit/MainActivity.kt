package com.catit

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.catit.view.HomeActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import java.util.*

class MainActivity : AppCompatActivity() {
  private lateinit var imageViewLogo: ImageView
  private lateinit var adBanner: AdView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    imageViewLogo = findViewById(R.id.imageViewLogo)
    adBanner = findViewById(R.id.adViewBanner)

    adBanner.loadAd(AdRequest.Builder().build())

    AnimationUtils.loadAnimation(this, R.anim.bounce).also {
      imageViewLogo.startAnimation(it)
    }

    Timer().schedule(object : TimerTask() {
      override fun run() {
        runOnUiThread {
          val intent = Intent(this@MainActivity, HomeActivity::class.java)
          startActivity(intent)
          finish()
        }
      }
    }, 500)
  }
}