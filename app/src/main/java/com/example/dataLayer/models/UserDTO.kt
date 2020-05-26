package com.example.dataLayer.models

import com.google.gson.annotations.SerializedName

data class UserDTO(
        @SerializedName("id")
        val userID: Int,
        @SerializedName("displayName")
        val username: String,
        @SerializedName("email")
        val email: String
) {

    companion object {
        fun buildNullUserDTO() = UserDTO(userID = 0, username = "Unknown", email = "Unknown")
    }
}