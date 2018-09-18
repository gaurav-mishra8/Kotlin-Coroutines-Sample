package com.gaurav.imagelib

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

/**
 * Interface to provide result of image loading network request
 */
interface NetworkResponseCalback {
  fun onSuccess(inputStream: InputStream)

  fun onError()
}

/**
 * This class is responsible for making a network call to fetch bitmap image
 * from a url
 */
class NetworkDownloader {

  var client = OkHttpClient()

  fun loadUrl(
    url: String,
    callback: NetworkResponseCalback
  ) {
    val request = Request.Builder()
        .url(url)
        .build()

    launch {
      var inputStream: InputStream? = null

      val deferred = async {
        val response = client.newCall(request)
            .execute()
        if (response.isSuccessful) {
          inputStream = response.body()
              ?.byteStream()
          return@async inputStream
        } else {
          return@async inputStream
        }
      }

      deferred.await()?.let {
        callback.onSuccess(it)
      } ?: callback.onError()

    }
  }

}