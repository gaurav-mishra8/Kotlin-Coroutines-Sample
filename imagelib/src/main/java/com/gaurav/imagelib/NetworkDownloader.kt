package com.gaurav.imagelib

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

/**
 * This class is responsible for making a network call to fetch bitmap image
 * from a url
 */
class NetworkDownloader {

  private fun provideHttpClient() = OkHttpClient()

  /**
   * Make a netwrok request to load image from a url
   * @param url The url to fetch image from
   * @param callback Instance of {@link NetworkResponseCallback} to provide result
   */
  fun loadUrl(
    url: String
  ): Deferred<ByteArray?> {

    val request = Request.Builder()
        .url(url)
        .build()

    var byteArray: ByteArray? = null

    val deferred = async {
      val response = provideHttpClient().newCall(request)
          .execute()
      if (response != null && response.isSuccessful) {
        byteArray = response.body()
            ?.bytes()
        byteArray
      } else {
        byteArray
      }
    }
    return deferred
  }

  fun loadUrlStream(
    url: String
  ): Deferred<InputStream?> {

    val request = Request.Builder()
        .url(url)
        .build()

    var inputStream: InputStream? = null

    val deferred = async {
      val response = provideHttpClient().newCall(request)
          .execute()
      if (response.isSuccessful) {
        inputStream = response.body()
            ?.byteStream()
        return@async inputStream
      } else {
        return@async inputStream
      }
    }
    return deferred
  }
}