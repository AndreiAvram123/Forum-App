package com.socialMedia.dataLayer.models.serialization

import com.socialMedia.dataLayer.models.UserDTO
import com.google.gson.annotations.SerializedName

data class CommentDTO(
        @SerializedName("id")
        val id: Int,
        @SerializedName("postID")
        val postID: Int,
        @SerializedName("date")
        val date: Long,
        @SerializedName("content")
        val content: String,
        @SerializedName("user")
        val user: UserDTO

)