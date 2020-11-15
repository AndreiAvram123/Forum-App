package com.andrei.dataLayer.models

import com.google.gson.annotations.SerializedName

data class UpdateProfileImageRequest (
        @SerializedName("userID")
        val userID :String,
        @SerializedName("imageData")
        val imageData:String

)