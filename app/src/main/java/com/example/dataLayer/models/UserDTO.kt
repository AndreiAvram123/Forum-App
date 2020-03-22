package com.example.dataLayer.models

import com.google.gson.annotations.SerializedName

data class UserDTO(
        @SerializedName("userID")
        val userID: String,
        @SerializedName("username")
        val username: String,
        @SerializedName("email")
        val email: String?,
        @SerializedName("profilePicture")
        val profilePicture: String?
) {

    companion object {
        fun buildNullUserDTO() = UserDTO(userID = "", username = "Unknown", email = "Unknown", profilePicture = "Unknown")
    }
}