package com.andrei.dataLayer.interfaces

import com.andrei.dataLayer.models.UpdateProfileImageRequest
import com.andrei.dataLayer.models.UserDTO
import retrofit2.http.*

interface UserRepoInterface {
    @GET("/api/user/{userID}")
    suspend fun fetchUser(@Path("userID") userID: String): UserDTO

    @GET("/api/user/{userID}/friends")
    suspend fun fetchUserFriends(@Path("userID") userID: String): List<UserDTO>

    @POST("/api/user/changeProfilePicture")
    suspend fun updateProfilePicture( @Body updateProfileImageRequest: UpdateProfileImageRequest):UserDTO
}