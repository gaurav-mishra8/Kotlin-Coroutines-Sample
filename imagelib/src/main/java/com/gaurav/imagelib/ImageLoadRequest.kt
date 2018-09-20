package com.gaurav.imagelib

import android.net.Uri
import android.support.annotation.DrawableRes

/**
 * Data class to hold a single image loading request parameters
 */
data class ImageLoadRequest(
  val uri: Uri?, @DrawableRes val resId: Int,
  val reqWidth: Int?,
  val reqHeight: Int?
)