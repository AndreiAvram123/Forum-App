package com.andrei.dataLayer.interfaces

import com.andrei.kit.models.MessageDTO
import com.andrei.dataLayer.models.ChatDTO
import com.andrei.dataLayer.models.ServerResponse
import com.andrei.dataLayer.models.UserDTO
import com.andrei.dataLayer.models.deserialization.FriendRequest
import com.andrei.dataLayer.models.serialization.SerializeFriendRequest
import com.andrei.dataLayer.models.serialization.SerializeMessage
import retrofit2.http.*

//todo
//wrap in a response
interface ChatRepositoryInterface {

    @GET("/api/user/{userID}/chats")
    suspend fun fetchUserChats(@Path("userID") userID: String): List<ChatDTO>

    @POST("/api/messages/push")
    suspend fun pushMessage(@Body serializeMessage: SerializeMessage): MessageDTO


    @GET("/api/chat/{chatID}/recentMessages")
    suspend fun fetchRecentMessages(@Path("chatID") chatID: Int): List<MessageDTO>

    @GET("/api/chats/discover/{userID}")
    suspend fun fetchChatURL(@Path("userID") userID: String): ServerResponse

    @PATCH("/api/friendRequests/acceptRequest/{requestID}")
    suspend fun acceptFriendRequest(@Path("requestID") requestID: Int): ChatDTO


    @POST("/api/friendRequests/send")
    suspend fun sendFriendRequest(@Body friendRequest: SerializeFriendRequest):ServerResponse

    @GET("/api/user/{userID}/receivedRequests")
    suspend fun fetchFriendRequests(@Path("userID") userID: String): List<FriendRequest>


    @GET("/api/user/{userID}/friends")
    suspend fun fetchFriends(@Path("userID") userID: String): List<UserDTO>

    @GET("/api/user/{userID}/lastMessages")
    suspend fun fetchLastChatsMessage(@Path("userID") userID: String): List<MessageDTO>

}
