package com.gaurav.imagelib

import android.widget.ImageView
import java.io.InputStream

class RequestHandler(val networkDownloader: NetworkDownloader) {

  fun handleRequest(imageLoadRequest: ImageLoadRequest,callback: NetworkResponseCalback?) {
    networkDownloader.loadUrl(imageLoadRequest.uri.toString(),callback)
  }

}