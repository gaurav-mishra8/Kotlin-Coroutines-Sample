package com.gaurav.imagelib

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.support.annotation.DrawableRes
import java.io.File

class ImageLoader private constructor(val context: Context) {

  companion object {

    @SuppressLint("StaticFieldLeak")
    @Volatile
    internal var INSTANCE: ImageLoader? = null

    fun get(context: Context): ImageLoader {
      if (INSTANCE == null) {
        synchronized(ImageLoader::class.java) {
          if (INSTANCE == null) {
            INSTANCE = ImageLoader(context.applicationContext)
          }
        }
      }
      return INSTANCE!!
    }
  }

  fun load(uri: Uri): RequestConfig {
    return RequestConfig(this)
  }

  fun load(file: File): RequestConfig {
    return RequestConfig(this)
  }

  fun load(@DrawableRes resourceId: Int): RequestConfig {
    return RequestConfig(this)
  }

  fun load(url: String): RequestConfig {
    return load(Uri.parse(url))
  }

}