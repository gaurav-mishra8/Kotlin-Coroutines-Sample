package com.gaurav.imagelib

import android.net.Uri
import android.support.annotation.DrawableRes

class ImageLoadRequest(
  val uri: Uri?, @DrawableRes val resId: Int,
  val reqWidth: Int,
  val reqHeight: Int
)