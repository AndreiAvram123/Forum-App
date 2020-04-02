package com.example.dataLayer.RoomDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookapp.models.UserMessage

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE (senderID = :currentUserID AND receiverId = :user2ID )  OR (senderID = :user2ID AND receiverId=:currentUserID) ORDER BY messageDate DESC ")
    fun getMessages(currentUserID: String, user2ID: String): LiveData<List<UserMessage>>

    @Query("DELETE FROM messages WHERE (senderID = :currentUserID AND receiverId = :user2ID )  OR (senderID = :user2ID AND receiverId=:currentUserID)")
    suspend fun deleteOldData(currentUserID: String, user2ID: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<UserMessage>)

}
