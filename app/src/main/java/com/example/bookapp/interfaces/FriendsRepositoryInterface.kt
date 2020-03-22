package com.example.bookapp.interfaces

import com.example.dataLayer.models.FriendDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface FriendsRepositoryInterface {
    @GET("RestfulRequestHandler.php")
    fun getFriends(@Query("userID") userID: String?, @Query("friends") friends: Boolean, @Query("lastMessage") lastMessage: Boolean): Call<ArrayList<FriendDTO>>
}