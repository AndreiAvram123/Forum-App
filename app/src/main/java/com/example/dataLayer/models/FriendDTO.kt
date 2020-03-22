package com.example.dataLayer.models

import com.google.gson.annotations.SerializedName

data class FriendDTO(
        @SerializedName("userID")
        val userID: String,
        @SerializedName("username")
        val username: String,
        @SerializedName("profilePicture")
        val profilePicture: String,
        @SerializedName("lastMessage")
        val lastMessage: MessageDTO

) {
}