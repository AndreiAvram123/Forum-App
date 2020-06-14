package com.example.dataLayer.interfaces

import com.example.bookapp.models.LowDataPost
import com.example.dataLayer.models.PostDTO
import com.example.dataLayer.models.ServerResponse
import com.example.dataLayer.models.SerializeImage
import com.example.dataLayer.models.serialization.SerializePost
import retrofit2.http.*

interface PostRepositoryInterface {

    @GET("/recentPosts")
    suspend fun fetchRecentPosts(): ArrayList<PostDTO>

    @GET("/posts/page/{lastPostID}")
    suspend fun fetchNextPagePosts(@Path("lastPostID")lastPostID: Int): List<PostDTO>


    @GET("/post/{id}")
    suspend fun fetchPostByID(@Path("id") postID: Int): PostDTO

    @GET("/user/{id}/favoritePosts")
    suspend fun fetchUserFavoritePosts(@Path("id") userID: Int): ArrayList<PostDTO>


    @GET("/user/{id}/posts")
    suspend fun fetchMyPosts(@Path("id") userID: Int): ArrayList<PostDTO>

    @DELETE("/user/{userID}/removePost/{postID}")
    suspend fun removePostFromFavorites(@Path("userID") userID: Int, @Path("postID") postID: Int): ServerResponse

    @POST("/user/{userID}/addToFavorites/{postID}")
    suspend fun addPostToFavorites(@Path("postID") postID: Int, @Path("userID") userID: Int)

    @POST("/posts/uploadImage")
    suspend fun uploadImage(@Body serializeImage: SerializeImage): ServerResponse

    @POST("/posts/create")
    suspend fun uploadPost(@Body uploadPost: SerializePost): ServerResponse


}