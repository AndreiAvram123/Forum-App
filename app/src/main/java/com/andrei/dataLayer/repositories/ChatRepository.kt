package com.andrei.dataLayer.repositories

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.andrei.kit.models.Chat
import com.andrei.kit.models.User
import com.andrei.dataLayer.dataMappers.ChatMapper
import com.andrei.dataLayer.dataMappers.toMessage
import com.andrei.dataLayer.engineUtils.CallRunner
import com.andrei.dataLayer.engineUtils.ResponseHandler
import com.andrei.dataLayer.engineUtils.Result
import com.andrei.dataLayer.interfaces.ChatRepositoryInterface
import com.andrei.dataLayer.interfaces.dao.ChatDao
import com.andrei.dataLayer.interfaces.dao.MessageDao
import com.andrei.dataLayer.models.deserialization.FriendRequest
import com.andrei.dataLayer.models.serialization.SerializeFriendRequest
import com.andrei.dataLayer.models.serialization.SerializeMessage
import com.andrei.kit.utils.addAndNotify
import com.andrei.kit.utils.isConnected
import com.andrei.kit.utils.removeAndNotify
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
        private val responseHandler: ResponseHandler

) {

    private val callRunner = CallRunner(responseHandler)

    init {
        coroutineScope.launch {
            fetchLastChatsMessage()
        }
    }


    val receivedFriendRequests:MutableLiveData<MutableList<FriendRequest>> by lazy {
        MutableLiveData<MutableList<FriendRequest>>().also{
            coroutineScope.launch {
                fetchReceivedFriendRequests(user)
            }
        }
    }

    val sentFriendRequests:MutableLiveData<MutableList<FriendRequest>> by lazy {
        MutableLiveData<MutableList<FriendRequest>>().also {
            coroutineScope.launch {
                fetchSentFriendRequests()
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


    private suspend fun fetchLastChatsMessage() {
        try {
            val data = repo.fetchLastChatsMessage(user.userID)
            val messages = data.map { it.toMessage() }
            messageDao.insertMessages(messages)
        }catch (e:Exception){
            responseHandler.handleRequestException<Any>(e,Endpoint.LAST_CHAT_MESSAGES.url)
        }
    }

    private suspend fun fetchUserChats() {
        try {
            val fetchedData = repo.fetchUserChats(user.userID)
            val chats = ChatMapper.mapToDomainObjects(fetchedData, user.userID)
            chatDao.insert(chats)
        }catch(e:Exception){
             responseHandler.handleRequestException<Any>(e,Endpoint.USERS_CHAT.url)
            }
        }


     fun pushMessage(serializeMessage: SerializeMessage) = callRunner.makeObservableCall(repo.pushMessage(serializeMessage)) {
         messageDao.insertMessage(it.toMessage())
     }


    fun getCachedMessages(chatID: Int) = messageDao.getRecentChatMessages(chatID)


    private suspend fun fetchChatsLink() {
        callRunner.makeCall(repo.fetchChatURL(user.userID)) {
            chatLink.postValue(it.message)
        }
    }


     fun acceptFriendRequest(request: FriendRequest) = callRunner.makeObservableCall(repo.acceptFriendRequest(request.id)){
         val chat = ChatMapper.mapDtoObjectToDomainObject(it,request.receiver.userID)
         chatDao.insert(chat)
         receivedFriendRequests.removeAndNotify(request)
        }


    private suspend fun fetchReceivedFriendRequests(user: User) {
        if (connectivityManager.isConnected()) {
            val fetchedData = repo.fetchReceivedFriendRequests(user.userID)
            receivedFriendRequests.addAndNotify(fetchedData.toMutableList())
        }
    }
    private suspend fun fetchSentFriendRequests()  = callRunner.makeCall(repo.fetchSentFriendRequests(user.userID)) {
       sentFriendRequests.addAndNotify(it)

    }

      fun sendFriendRequest(friendRequest: SerializeFriendRequest) = callRunner.makeObservableCall(repo.sendFriendRequest(friendRequest)) {
         sentFriendRequests.addAndNotify(it)

      }


    fun fetchNewMessages(chatID: Int) = callRunner.makeObservableCall(repo.fetchRecentMessages(chatID)) {
        messageDao.insertMessages(it.map { messageDTO -> messageDTO.toMessage() })
    }

}