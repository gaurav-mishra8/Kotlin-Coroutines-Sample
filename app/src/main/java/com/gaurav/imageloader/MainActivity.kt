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

  val imageArray = arrayOf(
      "http://i.imgur.com/DvpvklR.png",
      "https://www.freepngimg.com/download/chair/46-chair-png-image.png",
      "https://www.freepngimg.com/download/tennis/8-2-tennis-png-picture.png",
      "http://i.imgur.com/DvpvklR.png",
      "https://www.freepngimg.com/thumb/pizza/49-pizza-png-image-thumb.png"
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    var index = 0
    tvLoadNext.setOnClickListener {
      if (index == imageArray.size) {
        index = 0
      }
      ImageLoader.get(applicationContext)
          .load(imageArray.get(index))
          .into(ivAvatar)
      index++
    }

  }

}
