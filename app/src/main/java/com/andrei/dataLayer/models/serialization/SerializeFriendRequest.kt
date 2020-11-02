package com.andrei.dataLayer.models.serialization

import com.google.gson.annotations.SerializedName

data class SerializeFriendRequest(
        @SerializedName("senderID")
        val senderID: String,
        @SerializedName("receiverID")
        val receiverID: String
)