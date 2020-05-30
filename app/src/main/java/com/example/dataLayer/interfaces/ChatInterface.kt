package com.example.dataLayer.interfaces

import com.example.dataLayer.models.ChatDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatInterface {

    @GET("/user/{userID}/chats")
    suspend fun fetchUserChats(userID: Int): List<ChatDTO>

    @FormUrlEncoded
    @POST("/push")
    suspend fun pushMessage(@Field("chatID") chatID: Int,
                            @Field("senderID") senderID: Int,
                            @Field("content") content: String)
}