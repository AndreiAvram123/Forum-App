package com.example.bookapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.models.Chat
import com.example.bookapp.models.User
import com.example.dataLayer.repositories.ChatRepository

class ViewModelChat : ViewModel() {
    private val chatRepository: ChatRepository = ChatRepository(viewModelScope)


    fun getUserChats(user: User): LiveData<List<Chat>> = chatRepository.fetchUserChats(user)

    fun sendMessage(chatID: Int, senderID: Int, content: String) {
        chatRepository.pushMessage(chatID, senderID, content)
    }

}