package com.andrew.dataLayer.interfaces

import com.andrew.dataLayer.models.ServerResponse
import com.andrew.dataLayer.models.UserDTO
import com.andrew.dataLayer.models.serialization.AuthenticationResponse
import retrofit2.http.*

interface UserRepositoryInterface {

    @FormUrlEncoded
    @POST("/api/register/google")
    suspend fun createGoogleAccount(@Field("uid") uid: String,
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
                         @Field("uid")uid:String)

    @GET("/api/token/{uid}")
    suspend fun getUserFomUID(@Path("uid") uid: String): AuthenticationResponse


}