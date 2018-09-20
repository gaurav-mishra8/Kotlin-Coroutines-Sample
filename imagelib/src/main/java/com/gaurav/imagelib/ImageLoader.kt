package com.gaurav.imagelib

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.support.annotation.DrawableRes

class ImageLoader private constructor(val context: Context) {

  val networkDownloader = NetworkDownloader()
  val imageTransformer = ImageTransformer(context)
  val imageCache = InMemoryCache()

  val requestHandler = RequestHandler(imageTransformer, imageCache, networkDownloader)

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

  fun load(uri: Uri): RequestBuilder {
    return RequestBuilder(this, uri, 0)
  }

  fun load(@DrawableRes resourceId: Int): RequestBuilder {
    return RequestBuilder(this, null, resourceId)
  }

  fun load(url: String): RequestBuilder {
    return load(Uri.parse(url))
  }

  fun submitRequest(
    imageLoadRequest: ImageLoadRequest,
    callback: ImageLoadingCallback?
  ) {
    requestHandler.handleRequest(imageLoadRequest, callback)
  }

}