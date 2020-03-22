package com.example.dataLayer.interfaces

import com.example.dataLayer.models.PostDTO
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface PostRepositoryInterface {
    @GET("RestfulRequestHandler.php")
    fun fetchPosts(@Query("recentPosts") recentPosts: Boolean): Call<ArrayList<PostDTO>>

    @GET("RestfulRequestHandler.php")
    fun fetchNewPosts(@Query("recentPosts") recentPosts: Boolean, @Query("lastPostID") lastPostID: Long): Call<ArrayList<PostDTO>>

    @GET("RestfulRequestHandler.php")
    fun fetchPostByID(@Query("postID") postID: Long): Call<PostDTO>

    @GET("RestfulRequestHandler.php")
    fun fetchFavoritePostsByUserID(@Query("userID") userID: String?, @Query("savedPosts") favoritePosts: Boolean): Call<ArrayList<PostDTO>>

    @GET("RestfulRequestHandler.php")
    fun fetchMyPosts(@Query("userID") userID: String?, @Query("myPosts") favoritePosts: Boolean): Call<ArrayList<PostDTO>>

    @POST("RestfulRequestHandler.php")
    fun uploadPost(@Query("uploadPost") uploadPost: Boolean, @Body serializePost: PostDTO?): Call<PostDTO>

    @FormUrlEncoded
    @POST("RestfulRequestHandler.php")
    fun addPostToFavorites(@Query("addPostToFavorite") addToFavorites: Boolean, @Field("postID") postID: Long, @Field("userID") userID: String?): Call<String>

    @FormUrlEncoded
    @POST("RestfulRequestHandler.php")
    fun removePostsFromFavorites(@Query("addPostToFavorite") addToFavorites: Boolean, @Field("postID") postID: Int, @Field("userID") userID: String?): Call<PostDTO>
}