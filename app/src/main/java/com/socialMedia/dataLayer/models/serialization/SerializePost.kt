package com.socialMedia.dataLayer.models.serialization

import com.google.gson.annotations.SerializedName

data class SerializePost(
        @SerializedName("title")
        val title: String,
        @SerializedName("content")
        val content: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("userID")
        val userID: Int
)