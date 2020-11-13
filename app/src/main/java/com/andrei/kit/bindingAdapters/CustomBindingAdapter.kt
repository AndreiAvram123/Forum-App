package com.andrei.kit.bindingAdapters

import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.andrei.dataLayer.models.deserialization.FriendRequestStatus
import com.andrei.kit.R
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("imageFromURL")
fun bindImageFromURL(imageView: ImageView,
                     imageURl: String?) {
    if (!imageURl.isNullOrEmpty()) {
        Glide.with(imageView).load(imageURl)
                .centerInside()
                .into(imageView)
    }
}


@BindingAdapter("photoViewImageFromURl")
fun getImage(photoView: PhotoView, imageURl: String?) {
    if (!imageURl.isNullOrEmpty()) {
        Glide.with(photoView).load(imageURl)
                .centerInside()
                .into(photoView)
    }
}
@BindingAdapter("firstImageFromString")
fun getFirstImageFromStringData(imageView: ImageView ,data: String?) {
    if (!data.isNullOrEmpty()) {
        val images = data.split(", ")
        Glide.with(imageView).load(images.first())
                .into(imageView)
    }
}

@BindingAdapter("dateFromUnix")
fun getDateFromUnix(textView: TextView, unixTime: Long) {
    if (unixTime > 0) {
        val compareDate = SimpleDateFormat("yyyy-MM-dd", Locale.UK)

        val nowDate = Date(Calendar.getInstance().timeInMillis)
        val messageDate = Date(unixTime * 1000)

        if (compareDate.format(nowDate) == compareDate.format(messageDate)) {
            val dateFormat = SimpleDateFormat("HH:mm", Locale.UK);
            textView.text = dateFormat.format(messageDate)
        } else {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            textView.text = dateFormat.format(messageDate)
        }
    }
}
