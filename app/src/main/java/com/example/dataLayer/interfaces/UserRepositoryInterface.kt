package com.example.dataLayer.interfaces

import com.example.dataLayer.models.UserDTO
import com.example.dataLayer.models.deserialization.DeserializeFriendRequest
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import retrofit2.http.*

interface UserRepositoryInterface {


    @GET("/user/{userID}/friends")
    suspend fun fetchFriends(@Path("userID") userID: Int): List<UserDTO>

    @FormUrlEncoded
    @POST("/api/login/google")
    suspend fun fetchGoogleUser(@Field("token") idToken: String,
                                @Field("username")
                                displayName: String,
                                @Field("email")
                                email: String): UserDTO

    @FormUrlEncoded
    @POST("/api/register/google")
    suspend fun createGoogleAccount(@Field("token") idToken: String,
                                    @Field("username")
                                    displayName: String,
                                    @Field("email")
                                    email: String): UserDTO

    @GET("/user/autocomplete/{query}")
    suspend fun fetchSuggestions(@Path("query") query: String): List<UserDTO>

    @Headers("Content-Type: application/json")
    @POST("/friendRequests/send")
    suspend fun pushFriendRequest(@Body friendRequest: SerializeFriendRequest)

    @GET("/user/{userID}/receivedRequests")
    suspend fun fetchFriendRequests(@Path("userID") userID: Int): List<DeserializeFriendRequest>

    @POST("/friendRequests/acceptRequest")
    @FormUrlEncoded
    suspend fun acceptFriendRequest(@Field("id") senderID: Int)

    @DELETE("/user/{userID}/removeFriend/{friendID}")
    suspend fun removeFriend(@Path("userID") userID: Int, @Path("friendID") friendID: Int)


}