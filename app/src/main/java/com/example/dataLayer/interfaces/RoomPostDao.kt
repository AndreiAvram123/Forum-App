package com.example.dataLayer.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dataLayer.models.RoomPostDTO

@Dao
interface RoomPostDao {
    @Query("SELECT * FROM posts WHERE postID = :postID")
    suspend fun getPostByID(postID: Long): RoomPostDTO

    @Query("SELECT * FROM posts")
    suspend fun getAllPosts():List<RoomPostDTO>

    @Insert
    suspend fun addPost(post: RoomPostDTO)
}