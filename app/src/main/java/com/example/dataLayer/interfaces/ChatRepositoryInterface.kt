package com.example.dataLayer.interfaces

import com.example.bookapp.models.MessageDTO
import com.example.dataLayer.models.ChatDTO
import com.example.dataLayer.models.ChatNotificationDTO
import com.example.dataLayer.models.ServerResponse
import com.example.dataLayer.models.UserDTO
import com.example.dataLayer.models.deserialization.FriendRequest
import com.example.dataLayer.models.serialization.SerializeFriendRequest
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

    @GET("/chats/discover/{userID}")
    suspend fun fetchChatURL(@Path("userID") userID: Int): ServerResponse

    @PATCH("/friendRequests/acceptRequest/{requestID}")
    suspend fun acceptFriendRequest(@Path("requestID") requestID: Int): ChatDTO

    @DELETE("/user/{userID}/removeFriend/{friendID}")
    suspend fun removeFriend(@Path("userID") userID: Int, @Path("friendID") friendID: Int)

    @POST("/friendRequests/send")
    suspend fun pushFriendRequest(@Body friendRequest: SerializeFriendRequest)

    @GET("/user/{userID}/receivedRequests")
    suspend fun fetchFriendRequests(@Path("userID") userID: Int): List<FriendRequest>


    @GET("/user/{userID}/friends")
    suspend fun fetchFriends(@Path("userID") userID: Int): List<UserDTO>

//    @GET("/user/{userID}/chatsNotifications")
//    suspend fun fetchChatNotification(@Path("userID") userID: Int): List<ChatNotificationDTO>

    @PATCH("/message/{messageID}/user/{userID}")
    suspend fun markMessageAsSeen(@Path("userID") userID: Int, @Path("messageID") messageID: Int)

    @GET("/notifications/discover/{userID}")
    suspend fun fetchNotificationLink(@Path("userID") userID: Int): ServerResponse

    @GET("/user/{userID}/chats/lastMessages")
    suspend fun fetchLastChatsMessage(@Path("userID") userID: Int): List<MessageDTO>

}
