package com.gaurav.imagelib

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.support.annotation.DrawableRes
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v4.widget.CircularProgressDrawable
import android.widget.ImageView

class RequestBuilder(
  val imageLoader: ImageLoader,
  val uri: Uri?,
  @DrawableRes val resId: Int
) {

  private var placeHolderRes: Int = 0
  private var errorRes: Int = 0
  private var placeholderDrawable: Drawable? = null
  private var errorDrawable: Drawable? = null

  /**
   * A placeholder resource to be shown while image is getting download
   * @param placeHolderRes The placeholder drawable to be used
   */
  fun setPlaceHolder(@DrawableRes placeHolderRes: Int): RequestBuilder {
    if (placeHolderRes == 0) {
      throw IllegalArgumentException("Invalid Placeholder resource")
    }
    this.placeHolderRes = placeHolderRes
    return this
  }

  /**
   * An error resource to be shown if image loading fails due to any reason
   * @param errorRes The resource drawable to be used for error scenario
   */
  fun setError(@DrawableRes errorRes: Int): RequestBuilder {
    if (errorRes == 0) {
      throw IllegalArgumentException("Invalid Error resource")
    }
    this.errorRes = errorRes
    return this
  }

  private fun getPlaceholderDrawable(): Drawable? {
    var placeHolder: Drawable? = null
    if (placeHolderRes != 0) {
      placeHolder = ContextCompat.getDrawable(imageLoader.context, placeHolderRes)
    } else {
      placeHolder = placeholderDrawable // This may be null which is expected and desired behavior.
    }
    return placeHolder
  }

  private fun getErrorDrawable(): Drawable {
    return if (errorRes != 0) {
      ContextCompat.getDrawable(imageLoader.context, errorRes)!!
    } else {
      return ContextCompat.getDrawable(
          imageLoader.context, R.drawable.ic_error
      )!! // This may be null which is expected and desired behavior.
    }
  }

  @RequiresApi(VERSION_CODES.JELLY_BEAN)
  fun into(imageView: ImageView) {
    val imageLoadRequest = createRequest()
    imageView.setImageDrawable(getPlaceholderDrawable())

    imageLoader.submitRequest(imageLoadRequest, object : NetworkResponseCalback {
      override fun onSuccess(byteArray: ByteArray) {
        imageView.let {
          val imageTransformer = ImageTransformer(imageLoader.context)
          val bitmap = imageTransformer.decodeByteArray(byteArray, it.width, it.height)
          bitmap?.let {
            val roundedBitmap = imageTransformer.getRoundedBitmap(bitmap)
            imageView.setImageDrawable(roundedBitmap)
          } ?: it.setImageDrawable(getErrorDrawable())
        }
      }

      override fun onError() {
        imageView.setImageDrawable(getErrorDrawable())
      }
    })
  }

  private fun createRequest(): ImageLoadRequest {
    return ImageLoadRequest(uri, resId, errorRes, placeHolderRes)
  }

}
