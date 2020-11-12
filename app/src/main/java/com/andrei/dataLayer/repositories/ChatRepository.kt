package com.andrei.dataLayer.repositories

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.andrei.kit.models.Chat
import com.andrei.kit.models.Message
import com.andrei.kit.models.User
import com.andrei.dataLayer.dataMappers.ChatMapper
import com.andrei.dataLayer.dataMappers.toMessage
import com.andrei.dataLayer.engineUtils.Resource
import com.andrei.dataLayer.engineUtils.ResponseHandler
import com.andrei.dataLayer.interfaces.ChatRepositoryInterface
import com.andrei.dataLayer.interfaces.dao.ChatDao
import com.andrei.dataLayer.interfaces.dao.MessageDao
import com.andrei.dataLayer.models.ChatNotificationDTO
import com.andrei.dataLayer.models.ServerResponse
import com.andrei.dataLayer.models.deserialization.FriendRequest
import com.andrei.dataLayer.models.serialization.SerializeFriendRequest
import com.andrei.dataLayer.models.serialization.SerializeMessage
import com.andrei.kit.utils.isConnected
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

    val friendRequests:MutableLiveData<MutableList<FriendRequest>> by lazy {
        MutableLiveData<MutableList<FriendRequest>>().also{
            coroutineScope.launch {
                fetchFriendRequests(user)
            }
        }
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
        emit(Resource.loading<Any>())
         try {
            val messageDTO  = repo.pushMessage(serializeMessage)
             messageDao.insertMessage(messageDTO.toMessage())
            emit(Resource.success(Any()))
        } catch (e: Exception) {
           responseHandler.handleException<Any>(e,Endpoint.PUSH_MESSAGE.url)
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



     fun acceptFriendRequest(request: FriendRequest) = liveData{
        emit(Resource.loading<Any>())
         try {
            val data = repo.acceptFriendRequest(request.id)
            val chat = ChatMapper.mapDtoObjectToDomainObject(data, request.receiver.userID)
            chatDao.insert(chat)
            friendRequests.value?.remove(request)
            emit(responseHandler.handleSuccess(Any()))
        }catch(e:Exception){
            responseHandler.handleException<Any>(e,"Accept friend request")
        }
        }


    private suspend fun fetchFriendRequests(user: User): ArrayList<FriendRequest> {
        if (connectivityManager.isConnected()) {
            return ArrayList(repo.fetchFriendRequests(user.userID))
        }
        return ArrayList()
    }

      fun sendFriendRequest(friendRequest: SerializeFriendRequest) = liveData {
        emit(Resource.loading<Any>())
        try {
            repo.sendFriendRequest(friendRequest)
            emit(Resource.success(Any()))
        }catch (e:Exception){
            emit(responseHandler.handleException<Any>(e,"Send friend request"))
        }
    }



    fun fetchNewMessages(chatID: Int) = liveData {
         try{
             val fetchedData = repo.fetchRecentMessages(chatID)
             val messages = fetchedData.map { it.toMessage() }
             messageDao.insertMessages(messages)
             emit(Resource.success(fetchedData))
         }catch (e:Exception){
             emit(responseHandler.handleException<Any>(e,"/api/chat/{chatID}/recentMessages"))
         }
    }


}