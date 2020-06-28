package com.example.dataLayer.models.serialization

import com.google.gson.annotations.SerializedName

data class SerializeComment(
        @SerializedName("content")
        val content: String,
        @SerializedName("postID")
        val postID: String,
        @SerializedName("userID")
        val userID: Int
)