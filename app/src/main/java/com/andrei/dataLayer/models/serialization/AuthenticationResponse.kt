package com.andrei.dataLayer.models.serialization

import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(
        @SerializedName("errors")
        val errors:List<String>?,
        @SerializedName("data")
        val authenticationData: AuthenticationData?
)