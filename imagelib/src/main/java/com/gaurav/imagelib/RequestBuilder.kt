package com.gaurav.imagelib

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.widget.ImageView
import com.gaurav.imagelib.R.drawable

class RequestBuilder(
  val imageLoader: ImageLoader,
  val uri: Uri?,
  @DrawableRes val resId: Int
) {

  private var placeHolderRes: Int = 0
  private var errorRes: Int = 0
  private var placeholderDrawable: Drawable? = null
  private lateinit var targetView: ImageView

  /**
   * A placeholder resource to be shown while image is getting download
   * @param placeHolderRes The placeholder drawable to be used
   */
  fun setPlaceHolder(@DrawableRes placeHolderRes: Int): RequestBuilder {
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

  fun into(imageView: ImageView) {
    targetView = imageView
    val imageLoadRequest = createRequest()

    showPlaceHolder()

    imageLoader.submitRequest(imageLoadRequest, object : ImageLoadingCallback {
      override fun onLoadingSuccess(drawable: RoundedBitmapDrawable) {
        targetView.setImageDrawable(drawable)
        targetView.background = null
      }

      override fun onLoadingError(
        url: String,
        exception: Exception
      ) {
        targetView.setImageResource(R.drawable.ic_error)
        targetView.background = null
      }
    })
  }

  private fun showPlaceHolder() {
    val animatedVectorDrawable =
      AnimatedVectorDrawableCompat.create(imageLoader.context, drawable.progress)
    targetView.background = animatedVectorDrawable

    val animatable = targetView.background as Animatable
    animatable.start()
    if (placeHolderRes != 0) {
      targetView.setImageResource(placeHolderRes)
    } else {
      targetView.setImageResource(drawable.ic_placeholder)
    }
  }

  private fun createRequest(): ImageLoadRequest {
    return ImageLoadRequest(uri, resId, targetView.width, targetView.height)
  }

}
