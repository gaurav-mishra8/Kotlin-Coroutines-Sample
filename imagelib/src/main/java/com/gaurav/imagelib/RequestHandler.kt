package com.gaurav.imagelib

import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

interface ImageLoadingCallback {
  fun onLoadingSuccess(drawable: RoundedBitmapDrawable)
  fun onLoadingError(
    url: String,
    exception: Exception
  )
}

/**
 * This class handles request for fetching an image from either local cache or network
 */
class RequestHandler(
  val imageTransformer: ImageTransformer,
  val imageCache: ImageCache,
  val networkDownloader: NetworkDownloader
) {

  var isRequestRunning = false

  /**
   * Fetch bitmap from cache or make a network call if needed using NetworkDownloader and transforms image bitmap
   * @param imageLoadRequest The request data for fetching image from network
   * @param callback a callback to send drawable back to listener
   */
  fun handleRequest(
    imageLoadRequest: ImageLoadRequest,
    callback: ImageLoadingCallback?
  ) {

    if (isRequestRunning) {
      return
    }
    val url = imageLoadRequest.uri.toString()
    var bitmap: Bitmap?

    // try getting bitmap from cache
    bitmap = imageCache.getImage(url)
    isRequestRunning = true

    try {
      // if bitmap not found in cache make netwrok call
      if (bitmap == null) {
        launch(UI) {
          val deferred = networkDownloader.loadUrl(url)
          val byteArray = deferred.await()
          bitmap = imageTransformer.decodeByteArray(
              byteArray, imageLoadRequest.reqWidth ?: 0, imageLoadRequest.reqHeight ?: 0
          )

          bitmap?.let {
            val roundedBitmap = getRoundedBitmap(bitmap!!)
            imageCache.putImage(url, it)
            callback?.onLoadingSuccess(roundedBitmap)
          } ?: callback?.onLoadingError(url, ImageLoadException("Error processing bitmap"))
        }
      } else {
        val roundedBitmap = getRoundedBitmap(bitmap!!)
        callback?.onLoadingSuccess(roundedBitmap)
      }

      bitmap?.recycle()

    } catch (e: Exception) {
      callback?.onLoadingError(url, e)
    } finally {
      isRequestRunning = false
    }

  }

  private fun getRoundedBitmap(bitmap: Bitmap) =
    imageTransformer.getRoundedBitmap(bitmap)
}