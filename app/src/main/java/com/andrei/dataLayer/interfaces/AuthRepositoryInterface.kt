package com.andrei.dataLayer.interfaces

import com.andrei.dataLayer.models.UserDTO
import com.andrei.dataLayer.models.serialization.AuthenticationResponse
import com.andrei.dataLayer.models.serialization.RegisterUserDTO
import retrofit2.Call
import retrofit2.http.*

interface AuthRepositoryInterface {


    @GET("/api/user/autocomplete/{query}")
    suspend fun fetchSuggestions(@Path("query") query: String): List<UserDTO>

    @POST("/api/register")
     fun register(@Body registerUserDTO: RegisterUserDTO): Call<AuthenticationResponse>

    @GET("/api/login/{uid}")
    suspend fun getUserFomUID(@Path("uid") uid: String): AuthenticationResponse


}