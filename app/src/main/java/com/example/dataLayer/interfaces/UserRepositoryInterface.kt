package com.example.dataLayer.interfaces

import androidx.annotation.Nullable
import com.example.dataLayer.models.ServerResponse
import com.example.dataLayer.models.UserDTO
import retrofit2.Call
import retrofit2.http.*

interface UserRepositoryInterface {


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

    @FormUrlEncoded
    @POST("/api/login")
    suspend fun login(@Field("username") username: String, @Field("password") password: String): UserDTO


    @GET("/user/autocomplete/{query}")
    suspend fun fetchSuggestions(@Path("query") query: String): List<UserDTO>

    @POST("/api/register")
    @FormUrlEncoded
    suspend fun register(@Field("username") username: String,
                         @Field("email") email: String,
                         @Field("password") password: String): ServerResponse


}