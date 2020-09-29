package com.andrew.bookapp.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
open class Message(
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
        var seenByCurrentUser: Boolean = false,
        @ColumnInfo(name = "localID")
        val localID: String?
){
        //todo
        //have local path here
        //todo
        //nopeeeeeeeeeeeeeeeeeee
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Message

                if (id != other.id) return false
                if (content != other.content) return false
                if (date != other.date) return false
                if (sender != other.sender) return false
                if (type != other.type) return false
                if (chatID != other.chatID) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id
                result = 31 * result + content.hashCode()
                result = 31 * result + date.hashCode()
                result = 31 * result + sender.hashCode()
                result = 31 * result + type.hashCode()
                result = 31 * result + chatID
                result = 31 * result + (localID?.hashCode() ?: 0)
                return result
        }
}