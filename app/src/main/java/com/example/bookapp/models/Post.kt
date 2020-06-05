package com.example.bookapp.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
        @PrimaryKey
        @ColumnInfo(name = "postID") val id: Int,
        @ColumnInfo(name = "postTitle") val title: String,
        @ColumnInfo(name = "postImage") val image: String,
        @ColumnInfo(name = "date") val date: Long,
        @ColumnInfo(name = "content") val content: String,
        @ColumnInfo(name = "isFavorite") var isFavorite: Boolean = false,
        @Embedded
        val author: User

) {

    companion object Empty {
        fun buildNullSafeObject(): Post {
            return Post(id = 0, content = "", date = 333, title = "", image = "dfd", author = User(userID = 0, username = "Unknown", email = "unknown", profilePicture = "sdfs"))
        }

        fun buildWaitingToUploadPost(): Post {
            return Post(id = -1, content = "", date = 333, title = "", image = "dfd", author = User(userID = 0, username = "Unknown", email = "unknown", profilePicture = "sdfs"))
        }
    }

}