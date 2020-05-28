package com.example.dataLayer.interfaces

import com.example.bookapp.models.Comment
import com.example.dataLayer.dataObjectsToSerialize.CommentDTO
import retrofit2.http.*
import java.util.*

interface CommentsInterface {
    @GET("/post/{postID}/comments")
    suspend fun fetchCommentsForPost(@Path("postID") postID: Int): ArrayList<CommentDTO>

    @POST("RestfulRequestHandler.php")
    suspend fun uploadComment(@Query("uploadComment") uploadComment: Boolean, @Body commentDTO: CommentDTO): Comment
}