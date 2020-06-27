package com.example.dataLayer.interfaces

import com.example.dataLayer.models.ServerResponse
import com.example.dataLayer.models.UserDTO
import com.example.dataLayer.models.serialization.AuthenticationResponse
import retrofit2.http.*

interface UserRepositoryInterface {


    @GET("/api/login/google/{token}")
    suspend fun fetchGoogleUser(@Path("token") token: String): AuthenticationResponse

    @FormUrlEncoded
    @POST("/api/register/google")
    suspend fun createGoogleAccount(@Field("token") idToken: String,
                                    @Field("username")
                                    displayName: String,
                                    @Field("email")
                                    email: String): AuthenticationResponse

    @FormUrlEncoded
    @POST("/api/login")
    suspend fun login(@Field("username") username: String, @Field("password") password: String): AuthenticationResponse


    @GET("/api/user/autocomplete/{query}")
    suspend fun fetchSuggestions(@Path("query") query: String): List<UserDTO>

    @POST("/api/register")
    @FormUrlEncoded
    suspend fun register(@Field("username") username: String,
                         @Field("email") email: String,
                         @Field("password") password: String): ServerResponse


}