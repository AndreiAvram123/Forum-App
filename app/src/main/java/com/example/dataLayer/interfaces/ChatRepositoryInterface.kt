package com.example.dataLayer.interfaces

import com.example.bookapp.models.MessageDTO
import com.example.dataLayer.models.ChatDTO
import com.example.dataLayer.models.ServerResponse
import com.example.dataLayer.models.serialization.SerializeMessage
import com.google.gson.annotations.SerializedName
import retrofit2.http.*

interface ChatRepositoryInterface {

    @GET("/user/{userID}/chats")
    suspend fun fetchUserChats(@Path("userID") userID: Int): List<ChatDTO>

    @POST("/push")
    suspend fun pushMessage(@Body serializeMessage: SerializeMessage): ServerResponse


    @GET("/chat/{chatID}/recentMessages")
    suspend fun fetchRecentMessages(@Path("chatID") chatID: Int): List<MessageDTO>

    @GET("/chat/discover/{chatID}")
    suspend fun fetchChatLink(@Path("chatID") chatID: Int): ChatLink

}

data class ChatLink(@SerializedName("hubURL")
                    val hubURL: String)
