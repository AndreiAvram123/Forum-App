package com.andrei.dataLayer.models

import com.google.gson.annotations.SerializedName


data class PostDTO(
        @SerializedName("id")
        val id: Int,
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