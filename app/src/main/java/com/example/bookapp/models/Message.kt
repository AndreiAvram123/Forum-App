package com.example.bookapp.models

import android.net.Uri
import androidx.room.*
import com.example.dataLayer.repositories.OperationStatus
import java.util.*

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
        var seenByCurrentUser: Boolean = false,
        @ColumnInfo(name = "localID")
        val localID: String?
) {
    @Ignore
    var resourcePath: Uri? = null

    @Ignore
    var operationStatus: OperationStatus = OperationStatus.FINISHED


    companion object {
        fun getMediaMessage(sender: User, type: String, chatID: Int, localId: String, path: Uri) = Message(id = -1, content = "", date = Calendar.getInstance().timeInMillis / 1000,
                sender = sender, type = type, chatID = chatID, seenByCurrentUser = true, localID = localId).apply {
            operationStatus = OperationStatus.ONGOING
            resourcePath = path
        }
    }
}