package com.example.dataLayer.models.serialization

import com.google.gson.annotations.SerializedName

data class SerializeFriendRequest(
        @SerializedName("senderID")
        val senderID: Int,
        @SerializedName("receiverID")
        val receiverID: Int
)