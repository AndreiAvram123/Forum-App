package com.example.dataLayer.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.models.Chat
import com.example.bookapp.models.MessageDTO
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.ChatMapper
import com.example.dataLayer.interfaces.ChatRepositoryInterface
import com.example.dataLayer.interfaces.ChatLink
import com.example.dataLayer.models.deserialization.FriendRequest
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import com.example.dataLayer.models.serialization.SerializeMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatRepository @Inject constructor(private val coroutineScope: CoroutineScope,
                                         private val repoInterface: ChatRepositoryInterface) {

    private val userChats: MutableLiveData<ArrayList<Chat>> by lazy {
        MutableLiveData<ArrayList<Chat>>()

    }
    private val chatMessages: MutableLiveData<List<MessageDTO>> by lazy {
        MutableLiveData<List<MessageDTO>>()
    }

    suspend fun fetchUserChats(user: User): LiveData<ArrayList<Chat>> {

        val fetchedData = repoInterface.fetchUserChats(user.userID)
        val chats = ChatMapper.mapDTOObjectsToDomainObjects(fetchedData, user.userID)
        userChats.postValue(ArrayList(chats))
        return userChats
    }

    fun pushMessage(serializeMessage: SerializeMessage) {
        coroutineScope.launch {
            try {
                repoInterface.pushMessage(serializeMessage)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getChatMessages(chatID: Int): MutableLiveData<List<MessageDTO>> {
        chatMessages.value = ArrayList()
        coroutineScope.launch {
            val fetchedData: List<MessageDTO> = repoInterface.fetchRecentMessages(chatID)
            chatMessages.postValue(fetchedData)
        }
        return chatMessages
    }

    fun getChatLink(chatID: Int): LiveData<ChatLink> {
        val liveDataLink = MutableLiveData<ChatLink>()
        coroutineScope.launch {
            val chatLink: ChatLink = repoInterface.fetchChatLink(chatID)
            liveDataLink.postValue(chatLink)
        }
        return liveDataLink;
    }

    suspend fun acceptFriendRequest(request: FriendRequest) {
        val chat = repoInterface.acceptFriendRequest(request.id)
        userChats.value?.add(ChatMapper.mapDtoObjectToDomainObject(chat, request.receiver.userID))
    }


    suspend fun fetchChatsNotification(user: User) = ArrayList(repoInterface.fetchChatNotification(user.userID))


    suspend fun fetchFriendRequests(user: User): ArrayList<FriendRequest> = ArrayList(repoInterface.fetchFriendRequests(user.userID))


    fun sendFriendRequest(friendRequest: SerializeFriendRequest) {
        coroutineScope.launch {
            repoInterface.pushFriendRequest(friendRequest)
        }
    }
}