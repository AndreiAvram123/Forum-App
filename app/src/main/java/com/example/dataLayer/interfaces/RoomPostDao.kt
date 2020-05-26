package com.example.dataLayer.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.dataLayer.models.UserWithPosts

@Dao
interface RoomPostDao {

    @Query("SELECT * FROM posts")
    fun getCachedPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE isFavorite ='1'")
    fun getFavoritePosts(): LiveData<List<Post>>
//
//    @Transaction
//    @Query("SELECT * FROM user")
//    fun getUserPosts(): LiveData<List<User>>

    @Query("SELECT * FROM posts WHERE postID = :postID LIMIT 1")
    suspend fun getPostByID(postID: Long): Post

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts:List<Post>)

    @Query("DELETE FROM posts")
    suspend fun removeCachedData()


    @Update
    suspend fun addPostToFavorites(post: Post)


    @Update
    suspend fun deletePostFromFavorites(post: Post)


}