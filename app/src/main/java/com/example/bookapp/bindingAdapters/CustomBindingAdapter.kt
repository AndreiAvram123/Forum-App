package com.example.bookapp.bindingAdapters

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
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

@BindingAdapter("dateFromUnix")
fun getDateFromUnix(textView: TextView, unixTime: Long) {
    if (unixTime > 0) {
        val compareDate: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK)

        val nowDate = Date(Calendar.getInstance().timeInMillis)
        val messageDate = Date(unixTime * 1000)

        if (compareDate.format(nowDate) == compareDate.format(messageDate)) {
            val dateFormat: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.UK);
            textView.text = dateFormat.format(messageDate)
        } else {
            val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            textView.text = dateFormat.format(messageDate)
        }
    }

}
//
//@BindingAdapter("backgroundNotification")
//fun showNotificationBackground(view:View, shouldShow: Boolean) {
//    if (shouldShow) {
//        view.setBackgroundColor(Color.YELLOW)
//    } else {
//        view.setBackgroundColor(Color.WHITE)
//    }
//}


