package com.gaurav.imageloader

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.gaurav.imagelib.ImageLoader

class MainActivity : AppCompatActivity() {

  val tvLoadNext by lazy { findViewById<TextView>(R.id.tv_next_avatar) }
  val ivAvatar by lazy { findViewById<ImageView>(R.id.iv_avatar) }

  lateinit var mainViewModel: MainViewModel

  val imageUrl = "http://i.imgur.com/DvpvklR.png"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    mainViewModel = ViewModelProviders.of(this)
        .get(MainViewModel::class.java)

    tvLoadNext.setOnClickListener {
      ImageLoader.get(this)
          .load(imageUrl)
          .into(ivAvatar)
    }

  }

}
