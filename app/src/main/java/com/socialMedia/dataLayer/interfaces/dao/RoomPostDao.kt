package com.socialMedia.dataLayer.interfaces.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.socialMedia.bookapp.models.Post
import com.socialMedia.dataLayer.models.UserWithFavoritePosts
import com.socialMedia.dataLayer.models.UserWithFavoritePostsCrossRef
import com.socialMedia.dataLayer.models.UserWithPosts


@Dao
interface RoomPostDao {

    @Query("SELECT * FROM post ORDER BY date DESC")
    fun getCachedPosts(): DataSource.Factory<Int, Post>

    @Query("SELECT * FROM user WHERE userID = :userID LIMIT 1")
    fun getFavoritePosts(userID: String): LiveData<UserWithFavoritePosts>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFavoritePosts(usersWithFavoritePostsCrossRef: List<UserWithFavoritePostsCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoritePost(usersWithFavoritePostsCrossRef: UserWithFavoritePostsCrossRef)

    @Delete
    suspend fun deletePostFromFavorites(usersWithFavoritePostsCrossRef: UserWithFavoritePostsCrossRef)


    @Query("SELECT * FROM user WHERE userID = :userID")
    fun getAllUserPosts(userID: String): LiveData<UserWithPosts>

    @Query("SELECT * FROM post WHERE postID = :postID LIMIT 1")
    fun getPostByID(postID: Int): LiveData<Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<Post>)

    @Query("DELETE FROM post")
    suspend fun removeCachedData()


    @Query("SELECT * FROM user WHERE userID = :userID LIMIT 1")
    suspend fun getFavoritePostsTest(userID: Int): UserWithFavoritePosts


}