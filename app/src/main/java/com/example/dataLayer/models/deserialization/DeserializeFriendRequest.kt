package com.example.dataLayer.models.deserialization

import com.example.dataLayer.models.UserDTO
import com.google.gson.annotations.SerializedName

data class DeserializeFriendRequest(
        @SerializedName("id")
        val id: Int,
        @SerializedName("sender")
        val sender: UserDTO,
        @SerializedName("receiver")
        val receiver: UserDTO
)