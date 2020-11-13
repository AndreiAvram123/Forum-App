package com.andrei.dataLayer.models.deserialization

import com.andrei.dataLayer.models.UserDTO
import com.google.gson.annotations.SerializedName

data class FriendRequest(
        @SerializedName("id")
        val id: Int,
        @SerializedName("sender")
        val sender: UserDTO,
        @SerializedName("receiver")
        val receiver: UserDTO
)
