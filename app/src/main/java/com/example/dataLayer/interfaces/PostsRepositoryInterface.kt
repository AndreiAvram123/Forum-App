package com.example.dataLayer.interfaces

import com.example.dataLayer.models.PostDTO
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

interface PostsRepositoryInterface {

    @GET("RestfulRequestHandler.php?recentPosts")
    suspend fun fetchPostByPage(@Query("page") page:Int = 1): ArrayList<PostDTO>

    @GET("RestfulRequestHandler.php")
    suspend fun fetchPostByID(@Query("postID") postID: Long,@Query("userID") userID: String = ""): PostDTO

    @GET("RestfulRequestHandler.php?favoritePosts")
   suspend fun fetchFavoritePostsByUserID(@Query("userID") userID: String): ArrayList<PostDTO>

    @GET("RestfulRequestHandler.php?myPosts")
    suspend fun fetchMyPosts(@Query("userID") userID: String?): ArrayList<PostDTO>

    @FormUrlEncoded
    @POST("RestfulRequestHandler.php?removePostFromFavorites")
    suspend fun deletePostFromFavorites(@Field("postID") postID: Long, @Field("userID") userID: String)

    @FormUrlEncoded
    @POST("RestfulRequestHandler.php?addPostToFavorites")
    suspend fun addPostToFavorites(@Field("postID") postID: Long, @Field("userID") userID: String)

}