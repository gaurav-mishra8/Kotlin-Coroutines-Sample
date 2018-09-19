package com.gaurav.imagelib

import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Interface to provide result of image loading network request
 */
interface NetworkResponseCalback {
  fun onSuccess(byteArray: ByteArray)

  fun onError()
}

/**
 * This class is responsible for making a network call to fetch bitmap image
 * from a url
 */
class NetworkDownloader {

  fun provideHttpClient() = OkHttpClient()

  fun loadUrl(
    url: String,
    callback: NetworkResponseCalback?
  ) {
    val request = Request.Builder()
        .url(url)
        .build()

    val uiContext: CoroutineContext = UI

    launch(uiContext) {
      var byteArray: ByteArray? = null

      val deferred = async {
        val response = provideHttpClient().newCall(request)
            .execute()
        if (response.isSuccessful) {
          byteArray = response.body()
              ?.bytes()
          return@async byteArray
        } else {
          return@async byteArray
        }
      }

      deferred.await()?.let {
        callback?.onSuccess(it)
      } ?: callback?.onError()

    }
  }

}