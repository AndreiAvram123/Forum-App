package com.example.dataLayer.repositories

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.bookapp.models.Chat
import com.example.bookapp.models.Message
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.ChatMapper
import com.example.dataLayer.dataMappers.MessageMapper
import com.example.dataLayer.interfaces.ChatRepositoryInterface
import com.example.dataLayer.interfaces.dao.ChatDao
import com.example.dataLayer.interfaces.dao.MessageDao
import com.example.dataLayer.models.ChatNotificationDTO
import com.example.dataLayer.models.deserialization.FriendRequest
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import com.example.dataLayer.models.serialization.SerializeMessage
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("MemberVisibilityCanBePrivate")
@Singleton
class ChatRepository @Inject constructor(
        private val repo: ChatRepositoryInterface,
        private val messageDao: MessageDao,
        private val chatDao: ChatDao,
        private val connectivityManager: ConnectivityManager,
        private val user: User,
        private val requestExecutor: RequestExecutor
) {


    private val chatNotifications by lazy {
        MutableLiveData<ArrayList<ChatNotificationDTO>>()
    }

    val userChats: LiveData<List<Chat>> by lazy {
        liveData {
            emitSource(chatDao.getChats())
            requestExecutor.add(this@ChatRepository::fetchUserChats, null)
        }
    }

    internal suspend fun fetchUserChats() {
        val fetchedData = repo.fetchUserChats(user.userID)
        val chats = ChatMapper.mapToDomainObjects(fetchedData, user.userID)
        chatDao.insert(chats)
    }


    suspend fun pushMessage(serializeMessage: SerializeMessage) {
        try {
            repo.pushMessage(serializeMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getChatMessages(chatID: Int): LiveData<List<Message>> =
            liveData {
                emitSource(messageDao.getRecentChatMessages(chatID))
                requestExecutor.add(this@ChatRepository::fetchChatMessages, chatID)
            }

    internal suspend fun fetchChatMessages(chatID: Int) {
        val fetchedData = repo.fetchRecentMessages(chatID)
        val messages = fetchedData.map { MessageMapper.mapToDomainObject(it) }
        messageDao.insertMessages(messages)
    }

    private val chatLink by lazy {
        MutableLiveData<String?>()
    }

    fun getChatLink(): LiveData<String?> {
        requestExecutor.add(this::fetchChatLink, user.userID)
        return chatLink
    }

    internal suspend fun fetchChatLink(userID: Int) {
        val link = repo.fetchChatURL(userID).message
        chatLink.postValue(link)
    }


    suspend fun acceptFriendRequest(request: FriendRequest) {
        val data = repo.acceptFriendRequest(request.id)
        val chat = ChatMapper.mapDtoObjectToDomainObject(data, request.receiver.userID)
        chatDao.insert(chat)
    }


    suspend fun fetchChatsNotification(user: User): MutableLiveData<ArrayList<ChatNotificationDTO>> {
        chatNotifications.value?.clear()
        if (connectivityManager.activeNetwork != null) {
            chatNotifications.postValue(ArrayList(repo.fetchChatNotification(user.userID)))
        }
        return chatNotifications
    }


    suspend fun fetchFriendRequests(user: User): List<FriendRequest> {
        if (connectivityManager.activeNetwork != null) {
            return repo.fetchFriendRequests(user.userID)
        }
        return ArrayList()
    }

    suspend fun sendFriendRequest(friendRequest: SerializeFriendRequest) {
        if (connectivityManager.activeNetwork != null) {
            repo.pushFriendRequest(friendRequest)
        }
    }

    suspend fun markMessageAsSeen(message: Message, user: User) {
        if (connectivityManager.activeNetwork != null) {
            repo.markMessageAsSeen(messageID = message.id,
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