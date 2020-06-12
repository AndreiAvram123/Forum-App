package com.example.bookapp.models

import android.graphics.drawable.Drawable
import com.example.dataLayer.models.UserDTO
import com.google.gson.annotations.SerializedName

open class MessageDTO(
        @SerializedName("id")
        val id: Int,
        @SerializedName("content")
        val content: String,
        @SerializedName("date")
        val date: Long,
        @SerializedName("sender")
        val sender: UserDTO,
        @SerializedName("type")
        val type: String,
        @SerializedName("chatID")
        val chatID: Int,
        @SerializedName("seenByUser")
        val seenByUser: Boolean,
        @SerializedName("localID")
        val localID: String?
)