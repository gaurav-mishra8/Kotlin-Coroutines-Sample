package com.gaurav.imageloader

import android.app.Application
import com.gaurav.imagelib.ImageLoader

class ImageApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    initImageLoader()
  }

  private fun initImageLoader() {
    ImageLoader.get(this)
  }
}