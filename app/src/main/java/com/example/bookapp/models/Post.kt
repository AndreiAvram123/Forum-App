package com.example.bookapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
        @PrimaryKey @ColumnInfo(name = "postID" )var postID: Long,
        @ColumnInfo(name = "postTitle") var postTitle: String,
        @ColumnInfo(name = "postImage") var postImage: String,
        @ColumnInfo (name = "postAuthorID") var postAuthorID:String? = null,
        @ColumnInfo(name = "postDate") var postDate: String? = null,
        @ColumnInfo(name = "postAuthor") var postAuthor: String? = null,
        @ColumnInfo(name = "postCategory") var postCategory: String? = null,
        @ColumnInfo(name = "postContent") var postContent: String? = null,
        @ColumnInfo(name = "isFavorite") var isFavorite:Boolean = false
) {

    companion object Empty {

        fun buildNullSafeObject(): Post {

            return Post(0, "Unknown", "","Unknown","Unknown","Unknown","Unknown");
        }
    }

}