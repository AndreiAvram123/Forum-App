package com.example.bookapp.models

import androidx.room.*

@Entity
data class Post(
        @PrimaryKey
        @ColumnInfo(name = "postID") val id: Int,
        @ColumnInfo(name = "postTitle") val title: String,
        @ColumnInfo(name = "postImage") val image: String,
        @ColumnInfo(name = "date") val date: Long,
        @ColumnInfo(name = "content") val content: String,
        @ColumnInfo(name = "isFavorite") var isFavorite: Boolean = false,
        @ColumnInfo(name = "authorID") val authorID: Int

) {
    @Ignore
    var author: User? = null

    companion object Empty {
        fun buildNullSafeObject(): Post {
            return Post(id = 0, content = "", date = 333, title = "", image = "dfd", authorID = 0)
        }

    }

}