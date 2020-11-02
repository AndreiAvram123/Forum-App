package com.andrei.kit.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class Message(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Int,
        @ColumnInfo(name = "content")
        val content: String,
        @ColumnInfo(name = "date")
        val date: Long,
        @Embedded
        val sender: User,
        @ColumnInfo(name = "type")
        val type: String,
        @ColumnInfo(name = "chatID")
        val chatID: Int,
        @ColumnInfo(name = "seenBy")
        var seenByCurrentUser: Boolean = false
)
