package com.gaurav.imagelib

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat

class RequestConfig(val imageLoader: ImageLoader) {

  private var placeHolderRes: Int = 0
  private var errorRes: Int = 0
  private var placeholderDrawable: Drawable? = null
  private var errorDrawable: Drawable? = null

  /**
   * A placeholder resource to be shown while image is getting download
   */
  fun setPlaceHolder(@DrawableRes placeHolderRes: Int): RequestConfig {
    if (placeHolderRes == 0) {
      throw IllegalArgumentException("Invalid Placeholder resource")
    }
    this.placeHolderRes = placeHolderRes
    return this
  }

  /**
   * An error resource to be shown if image loading fails due to any reason
   */
  fun setError(@DrawableRes errorRes: Int): RequestConfig {
    if (errorRes == 0) {
      throw IllegalArgumentException("Invalid Error resource")
    }
    this.errorRes = errorRes
    return this
  }

  private fun getPlaceholderDrawable(): Drawable? {
    return if (placeHolderRes != 0) {
      ContextCompat.getDrawable(imageLoader.context, placeHolderRes)
    } else {
      return placeholderDrawable // This may be null which is expected and desired behavior.
    }
  }

  private fun getErrorDrawable(): Drawable? {
    return if (errorRes != 0) {
      ContextCompat.getDrawable(imageLoader.context, errorRes)
    } else {
      return errorDrawable // This may be null which is expected and desired behavior.
    }
  }

}