package com.andrew.dataLayer.models.serialization

import com.andrew.dataLayer.models.UserDTO
import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(
        @SerializedName("user")
        val userDTO: UserDTO?,
        @SerializedName("token")
        val token: String?
)