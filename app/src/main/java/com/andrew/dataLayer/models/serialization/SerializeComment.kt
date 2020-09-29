package com.andrew.dataLayer.models.serialization

import com.google.gson.annotations.SerializedName

data class SerializeComment(
        @SerializedName("content")
        val content: String,
        @SerializedName("postID")
        val postID: Int,
        @SerializedName("userID")
        val userID: Int
)