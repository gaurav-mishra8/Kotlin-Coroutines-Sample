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

  val imageUrl =
    "https://www.google.co.in/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwieoaPPo8LdAhXCposKHT9QAy8QjRx6BAgBEAU&url=http%3A%2F%2Fmybestapizza.com%2F&psig=AOvVaw1uJ0g_EAPonON13FPw6q2J&ust=1537281911559104"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    mainViewModel = ViewModelProviders.of(this)
        .get(MainViewModel::class.java)

    tvLoadNext.setOnClickListener {

      ImageLoader.get(this)
          .load(imageUrl)
          .setPlaceHolder(R.drawable.ic_launcher_background)

    }

  }

}
