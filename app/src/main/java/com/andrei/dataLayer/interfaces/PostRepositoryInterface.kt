package com.andrei.dataLayer.interfaces

import com.andrei.dataLayer.models.PostDTO
import com.andrei.dataLayer.models.SerializeImage
import com.andrei.dataLayer.models.ServerResponse
import com.andrei.dataLayer.models.serialization.SerializePost
import com.andrei.dataLayer.models.serialization.SerializeFavoritePostRequest
import retrofit2.http.*

interface PostRepositoryInterface {

    @GET("/api/recentPosts")
    suspend fun fetchRecentPosts(): ArrayList<PostDTO>

    @GET("/api/posts/page/{lastPostID}")
    suspend fun fetchNextPagePosts(@Path("lastPostID")lastPostID: Int): List<PostDTO>


    @GET("/api/post/{id}")
    suspend fun fetchPostByID(@Path("id") postID: Int): PostDTO

    @GET("/api/user/{id}/favoritePosts")
    suspend fun fetchUserFavoritePosts(@Path("id") userID: String): ArrayList<PostDTO>


    @GET("/api/user/{id}/posts")
    suspend fun fetchMyPosts(@Path("id") userID: String): ArrayList<PostDTO>

    @DELETE("/api/user/{userID}/removeFromFavorites/{postID}")
    suspend fun removePostFromFavorites(@Path("userID") userID: String, @Path("postID") postID: Int): ServerResponse

    @POST("/api/posts/addToFavorites")
    suspend fun addPostToFavorites(@Body serializeFavoritePostRequest: SerializeFavoritePostRequest) :ServerResponse

    @POST("/api/posts/uploadImage")
    suspend fun uploadImage(@Body serializeImage: SerializeImage): ServerResponse

    @POST("/api/posts/create")
    suspend fun uploadPost(@Body uploadPost: SerializePost): ServerResponse


}