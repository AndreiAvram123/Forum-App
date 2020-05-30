package com.example.bookapp.viewModels

import androidx.lifecycle.*
import com.example.bookapp.models.Chat
import com.example.bookapp.models.MessageDTO
import com.example.bookapp.models.User
import com.example.dataLayer.repositories.ChatRepository

class ViewModelChat : ViewModel() {
    private val chatRepository: ChatRepository = ChatRepository(viewModelScope)

    val chat: MutableLiveData<Int> = MutableLiveData()


    val recentMessages: LiveData<List<MessageDTO>> = Transformations.switchMap(chat) {
        chat.value?.let {
            chatRepository.getChatMessages(it)
        }
    }

    fun getUserChats(user: User): LiveData<List<Chat>> = chatRepository.fetchUserChats(user)

    fun sendMessage(chatID: Int, senderID: Int, content: String) {
        chatRepository.pushMessage(chatID, senderID, content)
    }


}