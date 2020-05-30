package com.example.bookapp.models

import com.example.dataLayer.models.UserDTO
import com.google.gson.annotations.SerializedName

data class MessageDTO(
        @SerializedName("id")
        val id: Int,
        @SerializedName("content")
        val content: String,
        @SerializedName("date")
        val date: Long,
        @SerializedName("receiverID")
        val receiverID: Int,
        @SerializedName("sender")
        val sender: UserDTO
)