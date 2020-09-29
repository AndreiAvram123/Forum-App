package com.andrew.bookapp.models

import androidx.room.*

@Entity(tableName = "chat")
data class Chat(
        @PrimaryKey
        @ColumnInfo(name = "chatID")
        val chatID: Int,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "image")
        val image: String,
        @Embedded
        val user2: User
) {
    var newMessage: Boolean = false
}