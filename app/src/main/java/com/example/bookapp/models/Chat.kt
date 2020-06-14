package com.example.bookapp.models

import androidx.room.*

@Entity(tableName = "chat")
data class Chat(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val chatID: Int,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "image")
        val image: String,
        @Embedded
        val user2: User
)