package com.andrew.dataLayer.models.serialization

import com.google.gson.annotations.SerializedName

data class SerializeFavoritePostRequest (
        @SerializedName("postID")
        val postID : Int,
        @SerializedName("userID")
        val userID :String

)