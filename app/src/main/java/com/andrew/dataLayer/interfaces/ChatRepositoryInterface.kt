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
    suspend fun fetchUserChats(@Path("userID") userID: Int): List<ChatDTO>

    @POST("/api/messages/push")
    suspend fun pushMessage(@Body serializeMessage: SerializeMessage): ServerResponse


    @GET("/api/chat/{chatID}/recentMessages")
    suspend fun fetchRecentMessages(@Path("chatID") chatID: Int): List<MessageDTO>

    @GET("/api/chats/discover/{userID}")
    suspend fun fetchChatURL(@Path("userID") userID: Int): ServerResponse

    @PATCH("/api/friendRequests/acceptRequest/{requestID}")
    suspend fun acceptFriendRequest(@Path("requestID") requestID: Int): ChatDTO

    @DELETE("/api/user/{userID}/removeFriend/{friendID}")
    suspend fun removeFriend(@Path("userID") userID: Int, @Path("friendID") friendID: Int)

    @POST("/api/friendRequests/send")
    suspend fun pushFriendRequest(@Body friendRequest: SerializeFriendRequest)

    @GET("/api/user/{userID}/receivedRequests")
    suspend fun fetchFriendRequests(@Path("userID") userID: Int): List<FriendRequest>


    @GET("/api/user/{userID}/friends")
    suspend fun fetchFriends(@Path("userID") userID: Int): List<UserDTO>

    @PATCH("/api/messages/{messageID}/user/{userID}")
    suspend fun markMessageAsSeen(@Path("userID") userID: Int, @Path("messageID") messageID: Int)


    @GET("/api/user/{userID}/chats/lastMessages")
    suspend fun fetchLastChatsMessage(@Path("userID") userID: Int): List<MessageDTO>

}
