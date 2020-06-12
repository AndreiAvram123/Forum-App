package com.example.dataLayer.repositories

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.models.Chat
import com.example.bookapp.models.Message
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.ChatMapper
import com.example.dataLayer.dataMappers.MessageMapper
import com.example.dataLayer.interfaces.ChatRepositoryInterface
import com.example.dataLayer.interfaces.ChatLink
import com.example.dataLayer.interfaces.dao.ChatDao
import com.example.dataLayer.models.ChatNotificationDTO
import com.example.dataLayer.models.deserialization.FriendRequest
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import com.example.dataLayer.models.serialization.SerializeMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatRepository @Inject constructor(private val coroutineScope: CoroutineScope,
                                         private val repoInterface: ChatRepositoryInterface,
                                         private val chatDao: ChatDao,
                                         private val connectivityManager: ConnectivityManager
) {

    init {
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                //take action when network connection is gained
                  //todo
                //continue here
                    Log.d("haha", "uuuu")

            }

            override fun onLost(network: Network?) {
                //take action when network connection is lost
            }
        })

    }

    private val userChats: MutableLiveData<ArrayList<Chat>> by lazy {
        MutableLiveData<ArrayList<Chat>>()
    }


    private val chatNotifications by lazy {
        MutableLiveData<ArrayList<ChatNotificationDTO>>()
    }

    suspend fun fetchUserChats(user: User): LiveData<ArrayList<Chat>> {
        if (connectivityManager.activeNetwork != null) {
            val fetchedData = repoInterface.fetchUserChats(user.userID)
            userChats.postValue(ArrayList(ChatMapper.mapToDomainObjects(fetchedData, user.userID)))
        }
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

    fun getChatMessages(chatID: Int): LiveData<List<Message>> {
        if (connectivityManager.activeNetwork != null) {
            coroutineScope.launch {
                val fetchedData = repoInterface.fetchRecentMessages(chatID)
                val messages = fetchedData.map { MessageMapper.mapToDomainObject(it) }
                chatDao.insertMessages(messages)
            }
        }
        return chatDao.getRecentChatMessages(chatID)
    }

    suspend fun getChatLink(chatID: Int): ChatLink {
        if (connectivityManager.activeNetwork != null) {
            repoInterface.fetchChatLink(chatID)
        }
        return ChatLink("")
    }


    suspend fun acceptFriendRequest(request: FriendRequest) {
        val chat = repoInterface.acceptFriendRequest(request.id)
        userChats.value?.add(ChatMapper.mapDtoObjectToDomainObject(chat, request.receiver.userID))
    }


    suspend fun fetchChatsNotification(user: User): MutableLiveData<ArrayList<ChatNotificationDTO>> {
        chatNotifications.value?.clear()
        if (connectivityManager.activeNetwork != null) {
            chatNotifications.postValue(ArrayList(repoInterface.fetchChatNotification(user.userID)))
        }
        return chatNotifications
    }


    suspend fun fetchFriendRequests(user: User): List<FriendRequest> {
        if (connectivityManager.activeNetwork != null) {
            return repoInterface.fetchFriendRequests(user.userID)
        }
        return ArrayList()
    }

    fun sendFriendRequest(friendRequest: SerializeFriendRequest) {
        coroutineScope.launch {
            repoInterface.pushFriendRequest(friendRequest)
        }
    }

    suspend fun markMessageAsSeen(message: Message, user: User) {
        repoInterface.markMessageAsSeen(messageID = message.id,
                userID = user.userID)

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

    fun getLastMessage(chatID: Int): LiveData<Message> = chatDao.getLastMessage(chatID)
}