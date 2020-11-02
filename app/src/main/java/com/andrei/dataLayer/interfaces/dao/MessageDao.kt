package com.andrew.dataLayer.interfaces.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andrew.bookapp.models.Message

@Dao
interface MessageDao {

    @Query("SELECT * FROM message WHERE chatID = :chatID ORDER BY date DESC LIMIT 20")
    fun getRecentChatMessages(chatID: Int): LiveData<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<Message>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM message WHERE chatID = :chatID ORDER BY date DESC LIMIT 1")
    fun getLastMessage(chatID: Int): LiveData<Message>

    @Update
    suspend fun update(message: Message)
}