package com.andrei.bookapp.interfaces

import com.andrei.dataLayer.models.UserDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface FriendsRepositoryInterface {
    @GET("RestfulRequestHandler.php")
    fun getFriends(@Query("userID") userID: Int, @Query("friends") friends: Boolean, @Query("lastMessage") lastMessage: Boolean): Call<ArrayList<UserDTO>>
}