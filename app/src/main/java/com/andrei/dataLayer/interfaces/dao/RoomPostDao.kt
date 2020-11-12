package com.andrei.dataLayer.interfaces.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.andrei.kit.models.Post
import com.andrei.dataLayer.models.UserWithPosts


@Dao
interface RoomPostDao {

    @Query("SELECT * FROM post ORDER BY postID DESC")
    fun getCachedPosts(): DataSource.Factory<Int, Post>

    @Query("SELECT * FROM post WHERE isFavorite = 1")
    fun getFavoritePosts(): LiveData<List<Post>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePosts(posts: List<Post>)

    @Update(entity = Post::class)
    suspend fun updatePost (post:Post)


    @Query("SELECT * FROM user WHERE userID = :userID")
    fun getAllUserPosts(userID: String): LiveData<UserWithPosts>

    @Query("SELECT * FROM post WHERE postID = :postID LIMIT 1")
    fun getPostByID(postID: Int): LiveData<Post>

    @Query("SELECT * FROM post WHERE postID = :postID LIMIT 1")
    suspend fun getPostByIDSuspend(postID: Int): Post


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<Post>)


}