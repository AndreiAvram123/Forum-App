package com.andrei.dataLayer.interfaces

import com.andrei.kit.models.MessageDTO
import com.andrei.dataLayer.models.ChatDTO
import com.andrei.dataLayer.models.ServerResponse
import com.andrei.dataLayer.models.UserDTO
import com.andrei.dataLayer.models.deserialization.FriendRequest
import com.andrei.dataLayer.models.serialization.SerializeFriendRequest
import com.andrei.dataLayer.models.serialization.SerializeMessage
import retrofit2.Call
import retrofit2.http.*



interface ChatRepositoryInterface {

    @GET("/api/user/{userID}/chats")
    suspend fun fetchUserChats(@Path("userID") userID: String): List<ChatDTO>

    @POST("/api/messages/push")
     fun pushMessage(@Body serializeMessage: SerializeMessage): Call<MessageDTO>


    @GET("/api/chat/{chatID}/recentMessages")
     fun fetchRecentMessages(@Path("chatID") chatID: Int): Call<List<MessageDTO>>

    @GET("/api/chats/discover/{userID}")
     fun fetchChatURL(@Path("userID") userID: String): Call<ServerResponse>

    @PATCH("/api/friendRequests/acceptRequest/{requestID}")
     fun acceptFriendRequest(@Path("requestID") requestID: Int): Call<ChatDTO>


    @POST("/api/friendRequests/send")
     fun sendFriendRequest(@Body friendRequest: SerializeFriendRequest): Call<FriendRequest>

    @GET("/api/user/{userID}/receivedRequests")
    suspend fun fetchReceivedFriendRequests(@Path("userID") userID: String): List<FriendRequest>

    @GET("/api/user/{userID}/sentRequests")
      fun fetchSentFriendRequests(@Path("userID") userID: String): Call<List<FriendRequest>>

    @GET("/api/user/{userID}/lastMessages")
    suspend fun fetchLastChatsMessage(@Path("userID") userID: String): List<MessageDTO>

}
