package com.andrew.dataLayer.models.deserialization

import com.andrew.dataLayer.models.UserDTO
import com.google.gson.annotations.SerializedName

data class FriendRequest(
        @SerializedName("id")
        val id: Int,
        @SerializedName("sender")
        val sender: UserDTO,
        @SerializedName("receiver")
        val receiver: UserDTO
)