package com.andrei.dataLayer.models.serialization

import com.google.gson.annotations.SerializedName

data class SerializePost(
        @SerializedName("title")
        val title: String,
        @SerializedName("content")
        val content: String,
        @SerializedName("imageData")
        val imageData: List<String>,
        @SerializedName("userID")
        val userID: String,

)