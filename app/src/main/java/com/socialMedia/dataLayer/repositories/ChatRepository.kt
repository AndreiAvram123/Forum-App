package com.socialMedia.dataLayer.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.firebase.firestore.FirebaseFirestore
import com.socialMedia.bookapp.models.Chat
import com.socialMedia.bookapp.models.Message
import com.socialMedia.bookapp.models.User
import com.socialMedia.dataLayer.interfaces.dao.ChatDao
import com.socialMedia.dataLayer.interfaces.dao.MessageDao
import com.socialMedia.dataLayer.models.ChatNotificationDTO
import com.socialMedia.dataLayer.models.deserialization.FriendRequest
import com.socialMedia.dataLayer.models.serialization.SerializeFriendRequest
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")

class ChatRepository @Inject constructor(
        private val firebaseFirestore: FirebaseFirestore,
        private val messageDao: MessageDao,
        private val chatDao: ChatDao,
        private val user: User
) {


    private val chatNotifications by lazy {
        MutableLiveData<ArrayList<ChatNotificationDTO>>()
    }

    val userChats: LiveData<List<Chat>> by lazy {
        liveData {
            emitSource(chatDao.getChats())
          //  requestExecutor.add(this@ChatRepository::fetchUserChats, null)
        }
    }
    val chatLink by lazy {
        MutableLiveData<String?>()
    }.also {
     //   requestExecutor.add(this::fetchChatsLink, null)
    }

    val lastChatsMessage: LiveData<List<Int>> by lazy {
        chatDao.getLastChatsMessage()
    }.also {
      //  requestExecutor.add(this::fetchLastChatsMessage, null)
    }

    internal suspend fun fetchLastChatsMessage() {
//        val data = repo.fetchLastChatsMessage(user.userID)
//        val messages = data.map { it.toMessage() }
//        messageDao.insertMessages(messages)
    }

    internal suspend fun fetchUserChats() {
//        val fetchedData = repo.fetchUserChats(user.userID)
//        val chats = ChatMapper.mapToDomainObjects(fetchedData, user.userID)
//        chatDao.insert(chats)
    }



    fun getChatMessages(chatID: Int): LiveData<List<Message>> =
            liveData {
                emitSource(messageDao.getRecentChatMessages(chatID))
            //    requestExecutor.add(this@ChatRepository::fetchChatMessages, chatID)
            }

    internal suspend fun fetchChatMessages(chatID: Int) {
//        val fetchedData = repo.fetchRecentMessages(chatID)
//        val messages = fetchedData.map { it.toMessage() }
//        messageDao.insertMessages(messages)
    }


    internal suspend fun fetchChatsLink() {
//        val link = repo.fetchChatURL(user.userID).message
//        chatLink.postValue(link)
    }


    fun addNotification(notificationDTO: ChatNotificationDTO) {
        chatNotifications.value?.let {
            it.add(notificationDTO)
            chatNotifications.notifyObserver()
        }

    }

    suspend fun acceptFriendRequest(request: FriendRequest) {
//        val data = repo.acceptFriendRequest(request.id)
//        val chat = ChatMapper.mapDtoObjectToDomainObject(data, request.receiver.userID)
     //   chatDao.insert(chat)
    }


    suspend fun fetchFriendRequests(user: User): List<FriendRequest> {
         //   return repo.fetchFriendRequests(user.userID)
        return ArrayList()
    }

    suspend fun sendFriendRequest(friendRequest: SerializeFriendRequest) {
       //     repo.pushFriendRequest(friendRequest)

    }

    suspend fun markMessageAsSeen(message: Message, user: User) {
//            repo.markMessageAsSeen(messageID = message.id,
//                    userID = user.userID)
//            message.seenByCurrentUser = true
//            messageDao.update(message)
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