package com.socialMedia.bookapp.models

import com.google.gson.annotations.SerializedName

data class LowDataPost(
        @SerializedName("id")
        val id: Int,
        @SerializedName("postTitle")
        val title: String,
        @SerializedName("postImage")
        val image: String
)