package com.example.dataLayer.interfaces.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookapp.models.Message

@Dao
interface MessageDao {

    @Query("SELECT * FROM message WHERE chatID = :chatID ORDER BY date DESC LIMIT 20")
    fun getRecentChatMessages(chatID: Int): LiveData<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<Message>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessageCurrentThread(message: Message)

    @Query("SELECT * FROM message WHERE chatID = :chatID ORDER BY date DESC LIMIT 1")
    fun getLastMessage(chatID: Int): LiveData<Message>
}