package com.example.dataLayer.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class RoomPostDTO(
        @PrimaryKey val postID: Long,
        @ColumnInfo(name = "postTitle") val postTitle: String,
        @ColumnInfo(name = "postImage") val postImage: String,
        @ColumnInfo(name = "postDate")
        var postDate: String?,
        @ColumnInfo(name = "postAuthor")
        var postAuthor: String?,
        @ColumnInfo(name = "postCategory")
        var postCategory: String? = null,
        @ColumnInfo(name = "postContent")
        var postContent: String? = null
) {}
