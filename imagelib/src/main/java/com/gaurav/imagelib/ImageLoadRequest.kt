package com.gaurav.imagelib

import android.net.Uri
import android.support.annotation.DrawableRes

class ImageLoadRequest(
  val uri: Uri?, @DrawableRes val resId: Int,
  @DrawableRes val errorRes: Int = R.drawable.ic_error,
  @DrawableRes val placeHolderRes: Int = R.drawable.ic_placeholder
)