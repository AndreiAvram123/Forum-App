package com.example.dataLayer.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bookapp.models.Post

@Dao
interface RoomPostDao {
    @Query("SELECT * FROM posts WHERE postID = :postID LIMIT 1")
    suspend fun getPostByID(postID: Long): Post

    @Query("SELECT * FROM posts")
    fun getRecentPosts(): LiveData<List<Post>>

    @Insert
    suspend fun insertPost(post: Post)

    @Insert
    suspend fun insertPosts(posts:List<Post>)

    @Query("DELETE FROM posts ")
    suspend fun removeOldFetchedData()
}