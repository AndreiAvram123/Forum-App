package com.andrei.kit.models

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
        @Embedded val user: User,
        @ColumnInfo(name = "numberOfComments") var numberOfComments:Int ,
        @ColumnInfo(name = "isFavorite") var isFavorite: Boolean = false,
        @ColumnInfo(name = "bookmarkedTime") var bookmarkTimes:Int = 0
)