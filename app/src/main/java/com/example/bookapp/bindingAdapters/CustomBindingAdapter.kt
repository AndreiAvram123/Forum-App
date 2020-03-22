package com.example.bookapp.bindingAdapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("imageFromURL")
fun bindImageFromURL(imageView: ImageView, imageURl: String?) {
    if (!imageURl.isNullOrEmpty()) {
        Glide.with(imageView).load(imageURl)
                .centerInside()
                .into(imageView)
    }

}


