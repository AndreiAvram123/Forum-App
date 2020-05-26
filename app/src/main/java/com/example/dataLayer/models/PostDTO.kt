package com.example.dataLayer.models

import com.google.gson.annotations.SerializedName
import java.util.*


data class PostDTO(
        @SerializedName("id")
        val id: Long,
        @SerializedName("title")
        val title: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("date")
        val date: Long,
        @SerializedName("author")
        val author: UserDTO,
        @SerializedName("content")
        val content: String
)