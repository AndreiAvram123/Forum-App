package com.andrei.dataLayer.interfaces

import com.andrei.dataLayer.models.ServerResponse
import com.andrei.dataLayer.models.serialization.CommentDTO
import com.andrei.dataLayer.models.serialization.SerializeComment
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.*

interface CommentRepoInterface {
    @GET("/api/post/{postID}/comments")
    suspend fun fetchCommentsForPost(@Path("postID") postID: Int): ArrayList<CommentDTO>

    @POST("/api/comments/add")
    suspend fun uploadComment(@Body comment: SerializeComment): CommentDTO

    @GET("/api/comments/{commentID}")
    suspend fun fetchCommentById(@Path("commentID") id: Int): CommentDTO
}