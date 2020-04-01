package com.example.bookapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.example.bookapp.models.UserMessage
import com.example.dataLayer.repositories.MessageRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class ViewModelMessages(application: Application) : AndroidViewModel(application) {


    private val messageRepository: MessageRepository = MessageRepository(viewModelScope, application = getApplication())

    fun getRecentMessages(currentUserID: String, user2ID: String): LiveData<PagedList<UserMessage>> {
        if (messageRepository.requests[user2ID] == null) {
            viewModelScope.launch {
                messageRepository.fetchMessages(currentUserID, user2ID);
            }
        }
        return messageRepository.getMessages(currentUserID, user2ID)
    }
}