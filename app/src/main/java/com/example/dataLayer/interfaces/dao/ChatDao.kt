package com.example.dataLayer.interfaces.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookapp.models.Chat

@Dao
interface ChatDao {

    @Query("SELECT * FROM chat ")
    fun getChats(): LiveData<List<Chat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chats: List<Chat>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chat: Chat)

    @Query("SELECT DISTINCT chatID FROM message GROUP BY chatID HAVING seenBy = 0 ORDER BY id DESC ")
    fun getLastChatsMessage(): LiveData<List<Int>>

}