package com.example.dataLayer.interfaces

import com.example.dataLayer.models.MessageDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface MessagesRepositoryInterface {
    @GET("ChatController.php?apiKey=42239b8342a1fe81a71703f6de711073&requestName=fetchOldMessages")
    suspend fun fetchOldMessages(@Query("receiverId") receiverID: String,
                         @Query("currentUserId") currentUserID: String,
                         @Query("offset") offset: Int): ArrayList<MessageDTO>
}