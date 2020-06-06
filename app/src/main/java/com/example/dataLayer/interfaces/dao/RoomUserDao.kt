package com.example.dataLayer.interfaces.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.bookapp.models.User

@Dao
interface RoomUserDao {
    @Insert
    suspend fun insertUser(user: User)
}