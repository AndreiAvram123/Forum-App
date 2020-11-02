package com.andrei.dataLayer.models

import com.google.gson.annotations.SerializedName

data class ChatDTO(
        @SerializedName("id")
        val id: Int,
        @SerializedName("users")
        val users: List<UserDTO>
)