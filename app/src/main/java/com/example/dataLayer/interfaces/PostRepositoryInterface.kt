package com.example.dataLayer.interfaces

import com.example.dataLayer.models.PostDTO
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

interface PostRepositoryInterface {
//todo
    //refactor parameters


    @GET("RestfulRequestHandler.php?recentPosts")
    suspend fun fetchPostByPage(@Query("page") page:Int = 1): ArrayList<PostDTO>


    @GET("RestfulRequestHandler.php")
    suspend fun fetchPostByID(@Query("postID") postID: Long,@Query("userID") userID: String = ""): PostDTO

    @GET("RestfulRequestHandler.php")
    fun fetchFavoritePostsByUserID(@Query("userID") userID: String?, @Query("savedPosts") favoritePosts: Boolean): Call<ArrayList<PostDTO>>

    @GET("RestfulRequestHandler.php")
    fun fetchMyPosts(@Query("userID") userID: String?, @Query("myPosts") favoritePosts: Boolean): Call<ArrayList<PostDTO>>


    @FormUrlEncoded
    @POST("RestfulRequestHandler.php?addPostToFavorite")
    suspend fun addPostToFavorites(@Field("postID") postID: Long, @Field("userID") userID: String)

    @FormUrlEncoded
    @POST("RestfulRequestHandler.php")
    fun removePostsFromFavorites(@Query("addPostToFavorite") addToFavorites: Boolean, @Field("postID") postID: Int, @Field("userID") userID: String?): Call<PostDTO>
}