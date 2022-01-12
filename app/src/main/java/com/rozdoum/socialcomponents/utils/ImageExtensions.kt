package com.rozdoum.socialcomponents.utils

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.rozdoum.socialcomponents.R
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Matcher
import java.util.regex.Pattern

object ImageExtensions {

    @SuppressLint("PackageManagerGetSignatures")
    @Suppress("DEPRECATION")
    fun getKeyHash(activity:AppCompatActivity) {
        try {
            val info = activity.packageManager.getPackageInfo(
                activity.packageName, PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.i(
                        "YourKeyHash",
                        "YourKeyHash : " + Base64.encodeToString(md.digest(), Base64.DEFAULT)
                )
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }

    fun loadImage(imageView:ImageView,
        link: Any?,
        drawable: Placeholders = Placeholders.DEFAULT,
        isForCircle: Boolean = false,
        isOverride: Boolean = false,
        isFitCenter: Boolean = false,
        position: Int = 1
    ) {
//    Log.d("Vle Glide 1", "Vle Glide 1 $link")
        var requestOptions = RequestOptions()
        if (isOverride) {

//        requestOptions.fitCenter()
//        requestOptions.centerInside()
            val ivWidth = if (imageView.width < 1) 150 else imageView.width
            val ivHeight = if (imageView.height < 1) 150 else imageView.height
//        Timber.tag("ImagesValue").d(message = "ImageValues $ivHeight , $ivWidth")
//        Log.d("ImagesValue","ImageValues $ivHeight , $ivWidth")
//        requestOptions.format(DecodeFormat.PREFER_RGB_565)

//        requestOptions.override(ivWidth, ivHeight)//150
        }
        val thumb: Long = (position * 1000).toLong()
//    val options = RequestOptions().frame(thumb)
        if (!link.toString().imageFile()) {
            requestOptions = requestOptions.frame(thumb)
        }

        requestOptions = requestOptions.error(drawable.intValue)
        requestOptions = requestOptions.placeholder(drawable.intValue)
        requestOptions = requestOptions.signature(ObjectKey(link ?: ""))
        try {
            if (isForCircle) {
                requestOptions = requestOptions.circleCrop()
                GlideApp.with(imageView)
                    .load(link)
                    .circleCrop()
                    .apply(requestOptions)
                    .placeholder(drawable.intValue)//circularProgressDrawable
                    .error(drawable.intValue)
                    .signature(ObjectKey(link ?: ""))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)
            } else {
                requestOptions = if (isFitCenter) {
                    requestOptions.fitCenter()
                } else {
                    requestOptions.centerCrop()
                }
                GlideApp.with(imageView)
                    .load(link)
                    .apply(requestOptions)
                    .placeholder(drawable.intValue)//circularProgressDrawable
                    .error(drawable.intValue)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .signature(ObjectKey(link ?: ""))
                    .into(imageView)
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }


    }
}



enum class Placeholders(var intValue: Int) {
    DEFAULT(R.drawable.posts_counter_bg),
    CIRCLE(R.drawable.posts_counter_bg)

}
fun String.imageFile(): Boolean {
    // Regex to check valid image file extension.
    val regex =
        "([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp|svg|jpg|jpeg|ico|cur|tif|tiff|jfif|pjpeg|pjp|apng|avif|webp))$)"

    // Compile the ReGex
    val p: Pattern = Pattern.compile(regex)

    // If the string is empty
    // return false
    if (this == null) {
        return false
    }

    // Pattern class contains matcher() method
    // to find matching between given string
    // and regular expression.
    val m: Matcher = p.matcher(this)

    // Return if the string
    // matched the ReGex
    return m.matches()
}