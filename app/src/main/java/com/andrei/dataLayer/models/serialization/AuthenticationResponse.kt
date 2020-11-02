package com.andrei.dataLayer.models.serialization

import com.andrei.dataLayer.models.UserDTO
import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(
        @SerializedName("user")
        val userDTO: UserDTO?,
        @SerializedName("token")
        val token: String?
)