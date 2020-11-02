package com.andrew.dataLayer.models.serialization

import com.google.gson.annotations.SerializedName

data class SerializeMessage(
        @SerializedName("type")
        val type: String,
        @SerializedName("chatID")
        val chatID: Int,
        @SerializedName("senderID")
        val senderID: String,
        @SerializedName("content")
        val content: String,
        @SerializedName("localID")
        val localIdentifier: String?
)