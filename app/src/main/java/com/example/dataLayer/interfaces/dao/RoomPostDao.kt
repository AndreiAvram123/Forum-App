package com.example.dataLayer.interfaces.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bookapp.models.Post

@Dao
interface RoomPostDao {

    @Query("SELECT * FROM post")
    fun getCachedPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM post WHERE isFavorite ='1'")
    fun getFavoritePosts(): LiveData<List<Post>>


    @Query("SELECT * FROM post WHERE postID = :postID LIMIT 1")
    suspend fun getPostByID(postID: Int): Post

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts:List<Post>)

    @Query("DELETE FROM post")
    suspend fun removeCachedData()


    @Update
    suspend fun addPostToFavorites(post: Post)


    @Update
    suspend fun deletePostFromFavorites(post: Post)


}