package com.andrew.dataLayer.models

import com.google.gson.annotations.SerializedName

data class ServerResponse(
        @SerializedName("RequestCompleted")
        val successful: Boolean,
        @SerializedName("Message")
        val message: String,
        @SerializedName("errors")
        val errors: List<String>?
)