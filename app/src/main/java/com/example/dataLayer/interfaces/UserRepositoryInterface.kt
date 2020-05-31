package com.example.dataLayer.interfaces

import com.example.dataLayer.models.UserDTO
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


}