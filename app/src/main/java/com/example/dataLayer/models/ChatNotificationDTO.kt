package com.example.dataLayer.models

import com.google.gson.annotations.SerializedName

data class ChatNotificationDTO(
        @SerializedName("chatID")
        val chatID: Int,
        @SerializedName("lastMessageSeen")
        val lastMessageSeen: Boolean
)