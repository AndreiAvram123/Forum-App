package com.example.dataLayer.RoomDao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookapp.models.UserMessage
import org.jetbrains.annotations.NotNull

@Dao
interface MessageDao {

    @Query("SELECT * FROM messages WHERE (senderID = :currentUserID AND receiverId = :user2ID )  OR (senderID = :user2ID AND receiverId=:currentUserID) ORDER BY messageID ")
    fun getAllMessages(currentUserID: @NotNull String, user2ID: @NotNull String): DataSource.Factory<Int, UserMessage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(userMessages: List<UserMessage>)

}
