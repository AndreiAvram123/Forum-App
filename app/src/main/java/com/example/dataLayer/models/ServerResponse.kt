package com.example.dataLayer.models

import com.google.gson.annotations.SerializedName

data class ServerResponse(
        @SerializedName("message")
        val message: String
)