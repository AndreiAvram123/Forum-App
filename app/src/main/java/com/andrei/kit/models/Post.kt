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
        @ColumnInfo(name = "isFavorite") var isFavorite: Boolean = false,
        @ColumnInfo(name = "numberOfComments") val numberOfComments:Int,
        @ColumnInfo(name = "bookmarkedTime") val bookmarkTimes:Int,
        @Embedded val user: User



)