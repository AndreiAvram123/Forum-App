package com.andrew.bookapp.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrew.bookapp.models.Chat
import com.andrew.bookapp.models.Message
import com.andrew.bookapp.models.User
import com.andrew.dataLayer.models.deserialization.FriendRequest
import com.andrew.dataLayer.models.serialization.SerializeFriendRequest
import com.andrew.dataLayer.models.serialization.SerializeMessage
import com.andrew.dataLayer.repositories.ChatRepository
import kotlinx.coroutines.launch


class ViewModelChat @ViewModelInject constructor(
        private val chatRepository: ChatRepository,
        private val user: User
) : ViewModel() {

    val currentChatId: MutableLiveData<Int> = MutableLiveData()

    val lastMessageChats: LiveData<List<Int>> by lazy {
        chatRepository.lastChatsMessage
    }


    val userChats: LiveData<List<Chat>> by lazy {
        chatRepository.userChats
    }


    fun refreshFriendRequests() {
        viewModelScope.launch {
            friendRequests.postValue(chatRepository.fetchFriendRequests(user))
        }
    }

    val friendRequests: MutableLiveData<ArrayList<FriendRequest>> by lazy {
        MutableLiveData<ArrayList<FriendRequest>>().also {
            viewModelScope.launch {
                val data = chatRepository.fetchFriendRequests(user)
                friendRequests.value = data
            }

        }
    }


    val recentMessages: LiveData<List<Message>> = Transformations.switchMap(currentChatId) {
        currentChatId.value?.let {
            chatRepository.getChatMessages(it)
        }
    }


    val chatLink: LiveData<String> = chatRepository.chatLink


    fun sendMessage(serializeMessage: SerializeMessage) = chatRepository.pushMessage(serializeMessage)

    fun sendFriendRequest(friendRequest: SerializeFriendRequest) {
        viewModelScope.launch {
            chatRepository.sendFriendRequest(friendRequest)
        }
    }

    fun acceptFriendRequest(request: FriendRequest) {
        friendRequests.value?.remove(request)
        viewModelScope.launch {
            chatRepository.acceptFriendRequest(request)
        }
    }

    fun markMessageAsSeen(message: Message, user: User) = viewModelScope.launch { chatRepository.markMessageAsSeen(message, user) }
}


