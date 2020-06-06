package com.example.dataLayer.interfaces

import com.example.bookapp.models.Comment
import com.example.dataLayer.dataObjectsToSerialize.CommentDTO
import com.example.dataLayer.models.ServerResponse
import com.example.dataLayer.models.serialization.SerializeComment
import retrofit2.http.*
import java.util.*

interface CommentsInterface {
    @GET("/post/{postID}/comments")
    suspend fun fetchCommentsForPost(@Path("postID") postID: Int): ArrayList<CommentDTO>

    @POST("/comments/add")
    suspend fun uploadComment(@Body comment: SerializeComment): ServerResponse

    @GET("/comments/{commentID}")
    suspend fun fetchCommentById(@Path("commentID") id: Int): CommentDTO
}