package com.example.dataLayer.interfaces

import com.example.dataLayer.models.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserRepositoryInterface {

    @GET("RestfulRequestHandler.php")
    fun authenticationWithThirdPartyEmail(@Query("authenticateThirdPartyAccount") thirdPartyAccount: Boolean,@Query("email") email:String): Call<UserDTO>

    @POST("RestfulRequestHandler.php")
    fun createThirdPartyAccount(@Query("createThirdPartyAccount") createAccount:Boolean,@Body userDTO: UserDTO):Call<UserDTO>

}