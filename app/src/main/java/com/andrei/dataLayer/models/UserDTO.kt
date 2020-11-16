package com.andrei.dataLayer.models

import com.google.gson.annotations.SerializedName

data class UserDTO(
        @SerializedName("id")
        val userID: String,
        @SerializedName("displayName")
        val username: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("profilePicture")
        val profilePicture: String
)