package com.example.dataLayer.repositories

import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.bookapp.models.Chat
import com.example.bookapp.models.Message
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.ChatMapper
import com.example.dataLayer.dataMappers.MessageMapper
import com.example.dataLayer.interfaces.ChatRepositoryInterface
import com.example.dataLayer.interfaces.ChatLink
import com.example.dataLayer.interfaces.dao.ChatDao
import com.example.dataLayer.interfaces.dao.MessageDao
import com.example.dataLayer.models.ChatNotificationDTO
import com.example.dataLayer.models.deserialization.FriendRequest
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import com.example.dataLayer.models.serialization.SerializeMessage
import javax.inject.Inject

class ChatRepository @Inject constructor(
        private val repoInterface: ChatRepositoryInterface,
        private val messageDao: MessageDao,
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

    fun fetchUserChats(user: User): LiveData<List<Chat>> =
            liveData {
                emitSource(chatDao.getChats())
                if (connectivityManager.activeNetwork != null) {
                    val fetchedData = repoInterface.fetchUserChats(user.userID)
                    val chats = ChatMapper.mapToDomainObjects(fetchedData, user.userID)
                    chatDao.insert(chats)
                }
            }


    suspend fun pushMessage(serializeMessage: SerializeMessage) {
        try {
            repoInterface.pushMessage(serializeMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getChatMessages(chatID: Int): LiveData<List<Message>> =
            liveData {
                emitSource(messageDao.getRecentChatMessages(chatID))

                if (connectivityManager.activeNetwork != null) {
                    val fetchedData = repoInterface.fetchRecentMessages(chatID)
                    val messages = fetchedData.map { MessageMapper.mapToDomainObject(it) }
                    messageDao.insertMessages(messages)
                }
            }

    suspend fun getChatLink(chatID: Int): ChatLink? {
        return if (connectivityManager.activeNetwork != null) {
            repoInterface.fetchChatLink(chatID)
        } else {
            return null
        }
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

    suspend fun sendFriendRequest(friendRequest: SerializeFriendRequest) {
        if (connectivityManager.activeNetwork != null) {
            repoInterface.pushFriendRequest(friendRequest)
        }
    }

    suspend fun markMessageAsSeen(message: Message, user: User) {
        if (connectivityManager.activeNetwork != null) {
            repoInterface.markMessageAsSeen(messageID = message.id,
                    userID = user.userID)
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
}