package com.socialMedia.dataLayer.interfaces.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.socialMedia.bookapp.models.Comment
import com.socialMedia.dataLayer.models.PostWithComments

@Dao
interface RoomCommentDao {
    @Transaction
    @Query("SELECT * FROM post WHERE postID = :postID LIMIT 1")
    fun getAllPostComments(postID: String): LiveData<PostWithComments>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<Comment>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment:Comment)

}