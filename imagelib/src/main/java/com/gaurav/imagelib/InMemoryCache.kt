package com.gaurav.imagelib

import android.graphics.Bitmap
import android.support.v4.util.LruCache

class InMemoryCache : ImageCache {

  private var mMemoryCache: LruCache<String, Bitmap>

  init {
    val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    val cacheSize = maxMemory / 8

    mMemoryCache = object : LruCache<String, Bitmap>(cacheSize) {
      override fun sizeOf(
        key: String,
        bitmap: Bitmap
      ): Int {
        return bitmap.byteCount / 1024
      }
    }
  }

  override fun getImage(key: String): Bitmap? {
    return mMemoryCache.get(key)
  }

  override fun putImage(
    key: String,
    bitmap: Bitmap
  ) {
    mMemoryCache.put(key, bitmap)
  }

  override fun clear() {
    mMemoryCache.evictAll()
  }
}