package com.socialMedia.dataLayer.models

import com.google.gson.annotations.SerializedName

data class UserDTO(
        @SerializedName("id")
        val userID: Int = 0,
        @SerializedName("displayName")
        val username: String = "",
        @SerializedName("email")
        val email: String = "",
        @SerializedName("profilePicture")
        val profilePicture: String = ""
)