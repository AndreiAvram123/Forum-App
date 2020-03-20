package com.example.bookapp.models

import com.google.gson.annotations.SerializedName

data class Post(@SerializedName("postID")

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

                var postContent: String? = null


) {

    var isFavorite = false

}