package com.socialMedia.dataLayer.models

import com.google.gson.annotations.SerializedName

data class ChatNotificationDTO(
        @SerializedName("chatID")
        val chatID: Int,
        @SerializedName("lastMessageSeen")
        var lastMessageSeen: Boolean = false
)