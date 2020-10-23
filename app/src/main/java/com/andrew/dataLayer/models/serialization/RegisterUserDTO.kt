package com.andrew.dataLayer.models.serialization

import com.google.gson.annotations.SerializedName

data class RegisterUserDTO (
        @SerializedName("displayName")
        val displayName:String,
        @SerializedName("email")
        val email :String,
        @SerializedName("uid")
        val uid:String
)