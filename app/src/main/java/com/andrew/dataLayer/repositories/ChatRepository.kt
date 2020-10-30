package com.andrew.dataLayer.repositories

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.andrew.bookapp.models.Chat
import com.andrew.bookapp.models.Message
import com.andrew.bookapp.models.User
import com.andrew.dataLayer.dataMappers.ChatMapper
import com.andrew.dataLayer.dataMappers.toMessage
import com.andrew.dataLayer.engineUtils.Resource
import com.andrew.dataLayer.engineUtils.ResponseHandler
import com.andrew.dataLayer.interfaces.ChatRepositoryInterface
import com.andrew.dataLayer.interfaces.dao.ChatDao
import com.andrew.dataLayer.interfaces.dao.MessageDao
import com.andrew.dataLayer.models.ChatNotificationDTO
import com.andrew.dataLayer.models.ServerResponse
import com.andrew.dataLayer.models.deserialization.FriendRequest
import com.andrew.dataLayer.models.serialization.SerializeFriendRequest
import com.andrew.dataLayer.models.serialization.SerializeMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatRepository @Inject constructor(
        private val repo: ChatRepositoryInterface,
        private val coroutineScope: CoroutineScope,
        private val messageDao: MessageDao,
        private val chatDao: ChatDao,
        private val connectivityManager: ConnectivityManager,
        private val user: User,

) {

    private val responseHandler = ResponseHandler()

    init {
        coroutineScope.launch {
            fetchLastChatsMessage()
        }
    }


    private val chatNotifications by lazy {
        MutableLiveData<ArrayList<ChatNotificationDTO>>()
    }

    val userChats: LiveData<List<Chat>> by lazy {
        liveData {
            emitSource(chatDao.getChats())
           fetchUserChats()
        }
    }
    val chatLink by lazy {
        MutableLiveData<String>().also{
            coroutineScope.launch {
                fetchChatsLink()
            }
        }
    }

    val lastChatsMessage: LiveData<List<Int>> by lazy {
        chatDao.getLastChatsMessage()
    }.also {
         coroutineScope.launch {
             fetchLastChatsMessage()
         }
    }

    private suspend fun fetchLastChatsMessage() {
        try {
            val data = repo.fetchLastChatsMessage(user.userID)
            val messages = data.map { it.toMessage() }
            messageDao.insertMessages(messages)
        }catch (e:Exception){
            responseHandler.handleException<Any>(e,Endpoint.LAST_CHAT_MESSAGES.url)
        }
    }

    private suspend fun fetchUserChats() {
        try {
            val fetchedData = repo.fetchUserChats(user.userID)
            val chats = ChatMapper.mapToDomainObjects(fetchedData, user.userID)
            chatDao.insert(chats)
        }catch(e:Exception){
             responseHandler.handleException<Any>(e,Endpoint.USERS_CHAT.url)
            }
        }


     fun pushMessage(serializeMessage: SerializeMessage) = liveData{
        emit(Resource.loading<ServerResponse>())
         try {
            val response  = repo.pushMessage(serializeMessage)
            emit(Resource.success(response))
        } catch (e: Exception) {
           responseHandler.handleException<ServerResponse>(e,Endpoint.PUSH_MESSAGE.url)
        }
    }


    fun getCachedMessages(chatID: Int) = messageDao.getRecentChatMessages(chatID)


    private suspend fun fetchChatsLink() {
        try {
            val link = repo.fetchChatURL(user.userID).message
            chatLink.postValue(link)
        }catch (e:Exception){
             responseHandler.handleException<Any>(e,Endpoint.CHAT_LINK.url)
        }
    }



    suspend fun acceptFriendRequest(request: FriendRequest) {
        val data = repo.acceptFriendRequest(request.id)
        val chat = ChatMapper.mapDtoObjectToDomainObject(data, request.receiver.userID)
        chatDao.insert(chat)
    }


    suspend fun fetchFriendRequests(user: User): ArrayList<FriendRequest> {
        if (connectivityManager.activeNetwork != null) {
            return ArrayList(repo.fetchFriendRequests(user.userID))
        }
        return ArrayList()
    }

    suspend fun sendFriendRequest(friendRequest: SerializeFriendRequest) {
        if (connectivityManager.activeNetwork != null) {
            repo.sendFriendRequest(friendRequest)
        }
    }

    suspend fun markMessageAsSeen(message: Message, user: User) {
        if (connectivityManager.activeNetwork != null) {
            repo.markMessageAsSeen(messageID = message.id,
                    userID = user.userID)
            message.seenByCurrentUser = true
            messageDao.update(message)
        }
        removeLocalNotification(message)
    }

    private fun removeLocalNotification(message: Message) {
        chatNotifications.value?.find { it.chatID == message.chatID }.also {
            if (it != null) {
                chatNotifications.value?.remove(it)
                chatNotifications.notifyObserver()
            }
        }
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

    fun fetchNewMessages(chatID: Int) = liveData {
         try{
             emit(Resource.loading<Any>())
             val fetchedData = repo.fetchRecentMessages(chatID)
             val messages = fetchedData.map { it.toMessage() }
             messageDao.insertMessages(messages)
             emit(Resource.success(fetchedData))
         }catch (e:Exception){
             emit(responseHandler.handleException<Any>(e,"/api/chat/{chatID}/recentMessages"))
         }
    }


}