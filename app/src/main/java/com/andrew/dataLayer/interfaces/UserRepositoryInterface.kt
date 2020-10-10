package com.andrew.dataLayer.interfaces

import com.andrew.dataLayer.models.UserDTO
import com.andrew.dataLayer.models.serialization.AuthenticationResponse
import retrofit2.http.*

interface UserRepositoryInterface {


    @GET("/api/user/autocomplete/{query}")
    suspend fun fetchSuggestions(@Path("query") query: String): List<UserDTO>

    @POST("/api/register")
    @FormUrlEncoded
    suspend fun register(@Field("username") username: String,
                         @Field("email") email: String,
                         @Field("uid")uid:String):AuthenticationResponse

    @GET("/api/login/{uid}")
    suspend fun getUserFomUID(@Path("uid") uid: String): AuthenticationResponse


}