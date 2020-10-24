package com.andrew.bookapp.models

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
        @ColumnInfo(name = "authorID") val authorID: String

) {
    @Ignore
    var author: User? = null
}