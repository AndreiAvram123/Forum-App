package com.example.dataLayer.interfaces

import com.example.dataLayer.models.ChatDTO
import retrofit2.http.GET

interface ChatInterface {

    @GET("/user/{userID}/chats")
    suspend fun fetchUserChats(userID: Int): List<ChatDTO>
}