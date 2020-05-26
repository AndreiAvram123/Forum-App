package com.example.bookapp.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
        @PrimaryKey @ColumnInfo(name = "postID" )var postID: Long,
        @ColumnInfo(name = "postTitle") var postTitle: String,
        @ColumnInfo(name = "postImage") var postImage: String,
        @ColumnInfo(name = "date") var postDate: Long,
        @ColumnInfo(name = "content") val postContent: String ,
        @ColumnInfo(name = "isFavorite") var isFavorite:Boolean = false,
        @ColumnInfo(name = "authorID") val authorID:Int
) {

    companion object Empty {
        fun buildNullSafeObject(): Post {
            return Post(postID = 0,postContent = "",postDate = 333,postTitle = "",postImage = "dfd",authorID = 3)
        }
    }

}