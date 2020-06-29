package com.socialMedia.bookapp.models

import androidx.room.*

@Entity
data class Post(
        @PrimaryKey
        @ColumnInfo(name = "postID") var id: String = "",
        @ColumnInfo(name = "postTitle") var title: String = "",
        @ColumnInfo(name = "postImage") var image: String = "",
        @ColumnInfo(name = "date") var date: Long = -10,
        @ColumnInfo(name = "content") var content: String = "",
        @ColumnInfo(name = "isFavorite") var isFavorite: Boolean = false,
        @ColumnInfo(name = "authorID") var authorID: String = ""

) {
    @Ignore
    var author: User? = null

    companion object Empty {
        fun buildTestPost(): Post {
            return Post(id = "", content = "", date = 333, title = "", image = "dfd", authorID = "")
        }

    }

}