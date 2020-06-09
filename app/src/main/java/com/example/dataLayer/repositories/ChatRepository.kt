package com.example.dataLayer.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.Chat
import com.example.bookapp.models.MessageDTO
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.ChatMapper
import com.example.dataLayer.interfaces.ChatRepositoryInterface
import com.example.dataLayer.interfaces.ChatLink
import com.example.dataLayer.models.serialization.SerializeMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatRepository @Inject constructor(private val coroutineScope: CoroutineScope) {

    val userChats: MutableLiveData<List<Chat>> by lazy {
        MutableLiveData<List<Chat>>()
    }
    private val chatMessages: MutableLiveData<List<MessageDTO>> = MutableLiveData()

    private val repositoryRepositoryInterface: ChatRepositoryInterface = AppUtilities.getRetrofit().create(ChatRepositoryInterface::class.java)

    fun fetchUserChats(user: User): LiveData<List<Chat>> {
        userChats.value = ArrayList()
        coroutineScope.launch {
            val fetchedData = repositoryRepositoryInterface.fetchUserChats(user.userID)
            userChats.postValue(ChatMapper.mapDTOObjectsToDomainObjects(fetchedData, user))
        }
        return userChats;
    }

    fun pushMessage(serializeMessage: SerializeMessage) {
        coroutineScope.launch {
            try {
                repositoryRepositoryInterface.pushMessage(serializeMessage)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getChatMessages(chatID: Int): MutableLiveData<List<MessageDTO>> {
        chatMessages.value = ArrayList()
        coroutineScope.launch {
            val fetchedData: List<MessageDTO> = repositoryRepositoryInterface.fetchRecentMessages(chatID)
            chatMessages.postValue(fetchedData)
        }
        return chatMessages
    }

    fun getChatLink(chatID: Int): LiveData<ChatLink> {
        val liveDataLink = MutableLiveData<ChatLink>()
        coroutineScope.launch {
            val chatLink: ChatLink = repositoryRepositoryInterface.fetchChatLink(chatID)
            liveDataLink.postValue(chatLink)
        }
        return liveDataLink;
    }
}