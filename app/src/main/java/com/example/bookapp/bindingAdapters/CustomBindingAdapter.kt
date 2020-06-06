package com.example.bookapp.bindingAdapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("imageFromURL")
fun bindImageFromURL(imageView: ImageView, imageURl: String?) {
    if (!imageURl.isNullOrEmpty()) {
        Glide.with(imageView).load(imageURl)
                .centerInside()
                .into(imageView)
    }
}

@BindingAdapter("dateFromUnix")
fun getDateFromUnix(textView: TextView, unixTime: Long) {
    val compareDate: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK)

    val nowDate = Date(Calendar.getInstance().timeInMillis)
    val messageDate = Date(unixTime * 1000)

    if (compareDate.format(nowDate) == compareDate.format(messageDate)) {
        val dateFormat: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.UK);
        textView.text = dateFormat.format(messageDate)
    } else {
        val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK);
        textView.text = dateFormat.format(messageDate)
    }


}


