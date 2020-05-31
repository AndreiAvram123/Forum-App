package com.example.dataLayer.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.Chat
import com.example.bookapp.models.MessageDTO
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.ChatMapper
import com.example.dataLayer.interfaces.ChatInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ChatRepository(private val coroutineScope: CoroutineScope) {

    val userChats: MutableLiveData<List<Chat>> by lazy {
        MutableLiveData<List<Chat>>()
    }
    private val chatMessages: MutableLiveData<List<MessageDTO>> = MutableLiveData()

    private val repositoryInterface: ChatInterface = AppUtilities.retrofit.create(ChatInterface::class.java)

    fun fetchUserChats(user: User): LiveData<List<Chat>> {
        coroutineScope.launch {
            val fetchedData = repositoryInterface.fetchUserChats(user.userID)
            userChats.postValue(ChatMapper.mapDTOObjectsToDomainObjects(fetchedData))
        }
        return userChats;
    }

    fun pushMessage(chatID: Int, senderID: Int, content: String) {
        coroutineScope.launch {
            try {
                repositoryInterface.pushMessage(chatID, senderID, content)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getChatMessages(chatID: Int): MutableLiveData<List<MessageDTO>> {
        chatMessages.value = ArrayList()
        coroutineScope.launch {
            val fetchedData: List<MessageDTO> = repositoryInterface.fetchRecentMessages(chatID)
            chatMessages.postValue(fetchedData)
        }
        return chatMessages
    }
}