package com.andrew.dataLayer.interfaces

import com.andrew.bookapp.models.MessageDTO
import com.andrew.dataLayer.models.ChatDTO
import com.andrew.dataLayer.models.ServerResponse
import com.andrew.dataLayer.models.UserDTO
import com.andrew.dataLayer.models.deserialization.FriendRequest
import com.andrew.dataLayer.models.serialization.SerializeFriendRequest
import com.andrew.dataLayer.models.serialization.SerializeMessage
import retrofit2.http.*

interface ChatRepositoryInterface {

    @GET("/api/user/{userID}/chats")
    suspend fun fetchUserChats(@Path("userID") userID: String): List<ChatDTO>

    @POST("/api/messages/push")
    suspend fun pushMessage(@Body serializeMessage: SerializeMessage): ServerResponse


    @GET("/api/chat/{chatID}/recentMessages")
    suspend fun fetchRecentMessages(@Path("chatID") chatID: Int): List<MessageDTO>

    @GET("/api/chats/discover/{userID}")
    suspend fun fetchChatURL(@Path("userID") userID: String): ServerResponse

    @PATCH("/api/friendRequests/acceptRequest/{requestID}")
    suspend fun acceptFriendRequest(@Path("requestID") requestID: Int): ChatDTO

    @DELETE("/api/user/{userID}/removeFriend/{friendID}")
    suspend fun removeFriend(@Path("userID") userID: String, @Path("friendID") friendID: String)

    @POST("/api/friendRequests/send")
    suspend fun sendFriendRequest(@Body friendRequest: SerializeFriendRequest):ServerResponse

    @GET("/api/user/{userID}/receivedRequests")
    suspend fun fetchFriendRequests(@Path("userID") userID: String): List<FriendRequest>


    @GET("/api/user/{userID}/friends")
    suspend fun fetchFriends(@Path("userID") userID: String): List<UserDTO>

    @PATCH("/api/messages/{messageID}/user/{userID}")
    suspend fun markMessageAsSeen(@Path("userID") userID: String, @Path("messageID") messageID: Int)


    @GET("/api/user/{userID}/chats/lastMessages")
    suspend fun fetchLastChatsMessage(@Path("userID") userID: String): List<MessageDTO>

}
