package com.example.dataLayer.interfaces

import com.example.dataLayer.models.MessageDTO
import retrofit2.http.*

interface MessagesRepositoryInterface {
    @GET("ChatController.php?apiKey=42239b8342a1fe81a71703f6de711073&requestName=fetchOldMessages")
    suspend fun fetchOldMessages(@Query("receiverId") receiverID: String,
                         @Query("currentUserId") currentUserID: String,
                         @Query("offset") offset: Int): ArrayList<MessageDTO>


    @FormUrlEncoded
    @POST("ChatController.php?requestName=sendMessage&apiKey=42239b8342a1fe81a71703f6de711073")
    suspend fun uploadTextMessage(@Field("currentUserId") currentUserID: String,
                                  @Field("receiverId") receiverID: String,
                                  @Field("messageContent") messageContent: String): MessageDTO

}