package com.gaurav.imagelib

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import java.io.InputStream

/**
 * This class is responsible for decoding bitmaps efficiently from different sources
 */
class ImageTransformer(val context: Context) {

  /**
   * Decodes an inputStream to provide bitmap
   * @param inputStream The inputstream for decoding a bitmap
   * @param targetWidth The target width of imageview in which bitmap is to be loaded
   * @param targetHeight The target height of imageView in which bitmap is to be loaded
   *
   * @return The decoded bitmap or null in case bitmap could not be decoded
   */
  fun decodeStream(
    inputStream: InputStream?,
    targetWidth: Int,
    targetHeight: Int
  ): Bitmap? {

    if (inputStream == null) {
      return null
    }

    var bitmap: Bitmap? = null

    try {
      val options = BitmapFactory.Options()
          .apply {
            inJustDecodeBounds = true
          }

      BitmapFactory.decodeStream(inputStream, null, options)
      inputStream.reset()
      options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)
      options.inJustDecodeBounds = false

      bitmap = BitmapFactory.decodeStream(inputStream, null, options)
    } catch (e: Exception) {
      e.printStackTrace()
    }

    return bitmap

  }

  /**
   * Get a rounded bitmap drawable for provided bitmap
   * @param src The bitmap to be rounded
   * @return The decoded bitmap or null in case bitmap could not be decoded
   */
  fun getRoundedBitmap(src: Bitmap): RoundedBitmapDrawable {
    val res = context.resources
    val dr = RoundedBitmapDrawableFactory.create(res, src)
    dr.isCircular = true
    return dr
  }

  /**
   * Decodes a byteArray to provide bitmap
   * @param byteArray The byteArray for decoding a bitmap
   * @param targetWidth The target width of imageview in which bitmap is to be loaded
   * @param targetHeight The target height of imageView in which bitmap is to be loaded
   *
   * @return The decoded bitmap or null in case bitmap could not be decoded
   */
  fun decodeByteArray(
    byteArray: ByteArray?,
    targetWidth: Int,
    targetHeight: Int
  ): Bitmap? {

    if (byteArray == null) {
      return null
    }

    if (targetHeight == 0 || targetWidth == 0) {
      return null
    }

    var bitmap: Bitmap? = null

    try {
      val options = BitmapFactory.Options()
          .apply {
            inJustDecodeBounds = true
          }

      BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
      options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)
      options.inJustDecodeBounds = false

      bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)

    } catch (e: Exception) {
      e.printStackTrace()
    }

    return bitmap

  }

  fun decodeSampledBitmapFromResource(
    res: Resources,
    resId: Int,
    reqWidth: Int,
    reqHeight: Int
  ): Bitmap {

    val options = BitmapFactory.Options()
        .apply {
          inJustDecodeBounds = true
        }

    BitmapFactory.decodeResource(res, resId, options)
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
    options.inJustDecodeBounds = false

    return BitmapFactory.decodeResource(res, resId, options)
  }

  /**
   * Calculates optimal samplesize to downsample image if needed
   * @param options BitmapFactory options to be used for calculating sample size
   * @param reqWidth The height of imageview that will hold the image
   * @param reqHeight The height of imageVeiw that will hold the image
   */
  private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int
  ): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

      val halfHeight: Int = height / 2
      val halfWidth: Int = width / 2

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
        inSampleSize *= 2
      }
    }

    return inSampleSize
  }

}