package com.example.dataLayer.RoomDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookapp.models.Message

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE (senderID = :currentUserID AND receiverId = :user2ID )  OR (senderID = :user2ID AND receiverId=:currentUserID) ORDER BY messageID  LIMIT 15")
    fun getRecentMessages(currentUserID: String, user2ID: String): LiveData<List<Message>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMessages(messages: List<Message>)

}
