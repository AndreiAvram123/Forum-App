package com.example.dataLayer.models

import com.google.gson.annotations.SerializedName

data class PostDTO(
        @SerializedName("postID")
        val postID: Long,
        @SerializedName("postTitle")
        var postTitle: String,
        @SerializedName("postImage")
        var postImage: String,
        @SerializedName("postDate")
        var postDate: String?,
        @SerializedName("postAuthor")
        var postAuthor: String?,
        @SerializedName("postCategory")
        var postCategory: String? = null,
        @SerializedName("postContent")
        var postContent: String? = null,
        @SerializedName("isFavorite")
        var isFavorite:Boolean = false,
        @SerializedName("postAuthorID")
        var postAuthorID: String?
)