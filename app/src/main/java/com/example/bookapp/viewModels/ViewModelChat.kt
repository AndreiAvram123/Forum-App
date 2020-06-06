package com.example.bookapp.viewModels

import androidx.lifecycle.*
import com.example.bookapp.models.Chat
import com.example.bookapp.models.MessageDTO
import com.example.bookapp.models.User
import com.example.dataLayer.interfaces.ChatLink
import com.example.dataLayer.repositories.ChatRepository

class ViewModelChat : ViewModel() {
    private val chatRepository: ChatRepository = ChatRepository(viewModelScope)

    val chatID: MutableLiveData<Int> = MutableLiveData()


    private var userChats: LiveData<List<Chat>>? = null


    val recentMessages: LiveData<List<MessageDTO>> = Transformations.switchMap(chatID) {
        chatID.value?.let {
            chatRepository.getChatMessages(it)
        }
    }
    val chatLink: LiveData<ChatLink> = Transformations.switchMap(chatID) {
        chatID.value?.let {
            chatRepository.getChatLink(it)
        }
    }


    fun getUserChats(user: User): LiveData<List<Chat>> {
        val temp = userChats
        if (temp != null) {
            return temp
        }
        return chatRepository.fetchUserChats(user)
    }


    fun sendMessage(chatID: Int, senderID: Int, content: String) {
        chatRepository.pushMessage(chatID, senderID, content)
    }


}