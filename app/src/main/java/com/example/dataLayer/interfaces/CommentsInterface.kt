package com.example.dataLayer.interfaces

import com.example.bookapp.models.Comment
import com.example.dataLayer.dataObjectsToSerialize.SerializeComment
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

interface CommentsInterface {
    @GET("RestfulRequestHandler.php")
    suspend fun fetchCommentsByPostID(@Query("postID") postID: Long, @Query("comments") fetchComments: Boolean = false): ArrayList<Comment?>

    @POST("RestfulRequestHandler.php")
    suspend fun uploadComment(@Query("uploadComment") uploadComment: Boolean, @Body comment: SerializeComment?): Comment
}