package com.gaurav.imagelib

import android.support.v4.graphics.drawable.RoundedBitmapDrawable

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
  val networkDownloader: NetworkDownloader
) {

  /**
   * Makes a network call using NetworkDownloader and transforms image bitmap
   * @param imageLoadRequest The request data for fetching image from network
   * @param callback a callback to send drawable back to listener
   */
  fun handleRequest(
    imageLoadRequest: ImageLoadRequest,
    callback: ImageLoadingCallback?
  ) {

    networkDownloader.loadUrl(imageLoadRequest.uri.toString(), object : NetworkResponseCallback {
      override fun onSuccess(byteArray: ByteArray) {
        val bitmap = imageTransformer.decodeByteArray(
            byteArray, imageLoadRequest.reqWidth, imageLoadRequest.reqHeight
        )
        bitmap?.let {
          val roundedBitmap = imageTransformer.getRoundedBitmap(bitmap)
          callback?.onLoadingSuccess(roundedBitmap)
        } ?: callback?.onLoadingError(
            imageLoadRequest.uri.toString(), ImageLoadException("Error generating bitmap drawable")
        )
      }

      override fun onError(
        url: String,
        exception: Exception
      ) {
        callback?.onLoadingError(url, exception)
      }
    })
  }

}