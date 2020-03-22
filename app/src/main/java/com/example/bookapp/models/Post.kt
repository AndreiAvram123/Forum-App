package com.example.bookapp.models

data class Post (
        var postID: Long,
        var postTitle: String,
        var postImage: String,
        var postDate: String? = null,
        var postAuthor: String? = null,
        var postCategory: String? = null,
        var postContent: String? = null
) {
    var isFavorite = false;

    class Builder {
        var postID: Long? = null
        var postTitle: String? = null
        var postImage: String? = null
        var postDate: String? = null
        var postAuthor: String? = null
        var postCategory: String? = null
        var postContent: String? = null
        fun build() = Post(postID!!, postTitle!!, postImage!!, postDate, postAuthor, postCategory, postContent);
    }

    companion object Empty {

        fun buildNullSafeObject(): Post {

            return Post(0, "Unknown", "","Unknown","Unknown","Unknown","Unknown");
        }
    }

}