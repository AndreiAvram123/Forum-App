package com.andrei.dataLayer.interfaces

import com.andrei.dataLayer.models.UserDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface UserRepoInterface {
    @GET("/api/user/{userID}")
    suspend fun fetchUser(@Path("userID") userID: String): UserDTO

    @GET("/api/user/{userID}/friends")
    suspend fun fetchUserFriends(@Path("userID") userID: String): List<UserDTO>
}