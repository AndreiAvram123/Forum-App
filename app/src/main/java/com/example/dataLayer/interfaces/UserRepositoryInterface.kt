package com.example.dataLayer.interfaces

import com.example.dataLayer.models.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserRepositoryInterface {

    @GET("RestfulRequestHandler.php?authenticateWithGoogle")
    suspend fun authenticationWithThirdPartyEmail(@Query("email") email:String): UserDTO

    @POST("RestfulRequestHandler.php?createThirdPartyAccount")
    suspend fun createThirdPartyAccount(@Body userDTO: UserDTO):UserDTO

}