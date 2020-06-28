package com.example.dataLayer.models

import com.google.gson.annotations.SerializedName


data class PostDTO(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("image")
        val image: String = "",
        @SerializedName("date")
        val date: Long = 0,
        val resourceURL: String = "",
        @SerializedName("author")
        val author: UserDTO = UserDTO(),
        @SerializedName("content")
        val content: String = ""
)