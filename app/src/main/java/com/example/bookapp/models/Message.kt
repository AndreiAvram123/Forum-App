package com.example.bookapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
        @PrimaryKey
        @ColumnInfo(name = "messageID")
        val messageID: String,
        @ColumnInfo(name = "messageContent")
        val messageContent: String?,
        @ColumnInfo(name = "senderID")
        val senderID: String,
        @ColumnInfo(name = "receiverId")
        val receiverID: String,
        @ColumnInfo(name = "messageDate")
        val messageDate: Long = 0L,
        @ColumnInfo(name = "messageImage")
        val messageImage: String?
) {
    companion object {
        fun getNullSafeObject():Message{
            return Message("0","Unknown","0","0",0L,"Unknown");
        }
    }
}
