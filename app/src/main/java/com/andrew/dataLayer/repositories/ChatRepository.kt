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
import com.andrew.dataLayer.interfaces.ChatRepositoryInterface
import com.andrew.dataLayer.interfaces.dao.ChatDao
import com.andrew.dataLayer.interfaces.dao.MessageDao
import com.andrew.dataLayer.models.ChatNotificationDTO
import com.andrew.dataLayer.models.deserialization.FriendRequest
import com.andrew.dataLayer.models.serialization.SerializeFriendRequest
import com.andrew.dataLayer.models.serialization.SerializeMessage
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")

class ChatRepository @Inject constructor(
        private val repo: ChatRepositoryInterface,
        private val messageDao: MessageDao,
        private val chatDao: ChatDao,
        private val connectivityManager: ConnectivityManager,
        private val user: User,
        private val requestExecutor: RequestExecutor
) {

    init {
        requestExecutor.addOnNetworkAvailable(this::fetchLastChatsMessage, null)
    }


    private val chatNotifications by lazy {
        MutableLiveData<ArrayList<ChatNotificationDTO>>()
    }

    val userChats: LiveData<List<Chat>> by lazy {
        liveData {
            emitSource(chatDao.getChats())
            requestExecutor.add(this@ChatRepository::fetchUserChats, null)
        }
    }
    val chatLink by lazy {
        MutableLiveData<String?>()
    }.also {
        requestExecutor.add(this::fetchChatsLink, null)

    }

    val lastChatsMessage: LiveData<List<Int>> by lazy {
        chatDao.getLastChatsMessage()
    }.also {
        requestExecutor.add(this::fetchLastChatsMessage, null)
    }

    internal suspend fun fetchLastChatsMessage() {
        val data = repo.fetchLastChatsMessage(user.userID)
        val messages = data.map { it.toMessage() }
        messageDao.insertMessages(messages)
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
        val messages = fetchedData.map { it.toMessage() }
        messageDao.insertMessages(messages)
    }


    internal suspend fun fetchChatsLink() {
        val link = repo.fetchChatURL(user.userID).message
        chatLink.postValue(link)
    }


    fun addNotification(notificationDTO: ChatNotificationDTO) {
        chatNotifications.value?.let {
            it.add(notificationDTO)
            chatNotifications.notifyObserver()
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


}