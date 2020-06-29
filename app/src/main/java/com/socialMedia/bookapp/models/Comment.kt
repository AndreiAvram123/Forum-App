package com.socialMedia.bookapp.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment")
data class Comment(
        @PrimaryKey
        @ColumnInfo(name = "commentID")
        val id: String,
        @ColumnInfo(name = "commentPostID")
        val postID: String,
        @ColumnInfo(name = "commentDate")
        val date: Long,
        @ColumnInfo(name = "commentContent")
        val content: String,
        @Embedded
        val user: User

) {

}