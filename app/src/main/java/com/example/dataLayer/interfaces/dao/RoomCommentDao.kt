package com.example.dataLayer.interfaces.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bookapp.models.Comment
import com.example.dataLayer.models.PostWithComments

@Dao
interface RoomCommentDao {
    @Transaction
    @Query("SELECT * FROM post WHERE postID = :postID LIMIT 1")
    fun getAllPostComments(postID: Int): LiveData<PostWithComments>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<Comment>)

}