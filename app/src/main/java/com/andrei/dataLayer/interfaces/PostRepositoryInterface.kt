package com.andrei.dataLayer.interfaces

import com.andrei.dataLayer.models.PostDTO
import com.andrei.dataLayer.models.SerializeImage
import com.andrei.dataLayer.models.ServerResponse
import com.andrei.dataLayer.models.serialization.SerializePost
import com.andrei.dataLayer.models.serialization.SerializeFavoritePostRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface PostRepositoryInterface {

    @GET("/api/recentPosts")
    fun fetchRecentPosts(): Call<ArrayList<PostDTO>>

    @GET("/api/posts/page/{lastPostID}")
    fun fetchNextPagePosts(@Path("lastPostID")lastPostID: Int): Call<List<PostDTO>>


    @GET("/api/post/{id}")
     fun fetchPostByID(@Path("id") postID: Int): Call<PostDTO>

    @GET("/api/user/{id}/favoritePosts")
    fun fetchUserFavoritePosts(@Path("id") userID: String): Call<ArrayList<PostDTO>>


    @GET("/api/user/{userID}/posts")
     fun fetchUserPosts(@Path("userID") userID: String): Call<ArrayList<PostDTO>>

    @DELETE("/api/user/{userID}/removeFromFavorites/{postID}")
     fun removePostFromFavorites(@Path("userID") userID: String, @Path("postID") postID: Int): Call<ServerResponse>

    @POST("/api/posts/addToFavorites")
    fun addPostToFavorites(@Body serializeFavoritePostRequest: SerializeFavoritePostRequest) :Call<ServerResponse>

    @POST("/api/posts/uploadImage")
    suspend fun uploadImage(@Body serializeImage: SerializeImage): ServerResponse

    @POST("/api/posts/create")
     fun uploadPost(@Body uploadPost: SerializePost): Call<PostDTO>


}