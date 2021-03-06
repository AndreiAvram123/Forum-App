package com.andrei.dataLayer.interfaces.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andrei.kit.models.Comment
import com.andrei.dataLayer.models.PostWithComments

@Dao
interface RoomCommentDao {
    @Transaction
    @Query("SELECT * FROM post WHERE postID = :postID LIMIT 1")
    fun getAllPostComments(postID: Int): LiveData<PostWithComments>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<Comment>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment:Comment)

}