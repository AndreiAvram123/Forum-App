package com.example.dataLayer.RoomDao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bookapp.models.Post

@Dao
interface RoomPostDao {

    @Query("SELECT * FROM posts")
    fun getRecentPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE isFavorite ='1'")
    fun getFavoritePosts(): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE postAuthorID = :userID")
     fun getUserPosts(userID: String?): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE postID = :postID LIMIT 1")
    suspend fun getPostByID(postID: Long): Post

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts:List<Post>)

    @Query("DELETE FROM posts")
    suspend fun removeOldFetchedData()


    @Update
    suspend fun addPostToFavorites(post: Post)


    @Update
    suspend fun deletePostFromFavorites(post: Post)


}