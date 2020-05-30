package com.example.dataLayer.interfaces

import com.example.bookapp.models.MessageDTO
import com.example.dataLayer.models.ChatDTO
import retrofit2.http.*

interface ChatInterface {

    @GET("/user/{userID}/chats")
    suspend fun fetchUserChats(userID: Int): List<ChatDTO>

    @FormUrlEncoded
    @POST("/push")
    suspend fun pushMessage(@Field("chatID") chatID: Int,
                            @Field("senderID") senderID: Int,
                            @Field("content") content: String)

    @GET("/chat/{chatID}/recentMessages")
    suspend fun fetchRecentMessages(@Path("chatID") chatID: Int): List<MessageDTO>

}