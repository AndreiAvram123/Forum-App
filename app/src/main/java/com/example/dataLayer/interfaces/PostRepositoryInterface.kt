package com.example.dataLayer.interfaces

import com.example.bookapp.models.LowDataPost
import com.example.dataLayer.models.PostDTO
import com.example.dataLayer.models.ServerResponse
import com.example.dataLayer.models.SerializeImage
import com.example.dataLayer.models.serialization.SerializePost
import retrofit2.http.*

interface PostRepositoryInterface {

    @GET("/posts/page/{page}")
    suspend fun fetchNextPage(@Path("page") page: Int): ArrayList<PostDTO>

    @GET("/post/{id}")
    suspend fun fetchPostByID(@Path("id") postID: Int): PostDTO

    @GET("/user/{id}/favoritePosts")
    suspend fun fetchFavoritePostsByUserID(@Path("id") userID: Int): ArrayList<PostDTO>

    @GET("/user/{id}/posts")
    suspend fun fetchMyPosts(@Path("id") userID: Int): ArrayList<PostDTO>

    @GET("/posts/autocomplete/{query}")
    suspend fun fetchSearchSuggestions(@Path("query") query: String): List<LowDataPost>

    @DELETE("/user/{userID}/removePost/{postID}")
    suspend fun deletePostFromFavorites(@Path("userID") postID: Int, @Path("postID") userID: Int)

    @POST("/user/{userID}/addToFavorites/{postID}")
    suspend fun addPostToFavorites(@Path("postID") postID: Int, @Path("userID") userID: Int)

    @POST("/posts/uploadImage")
    suspend fun uploadImage(@Body serializeImage: SerializeImage): ServerResponse

    @POST("/posts/create")
    suspend fun uploadPost(@Body uploadPost: SerializePost): ServerResponse


}