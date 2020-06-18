package com.example.bookapp.viewModels

import androidx.lifecycle.*
import com.example.bookapp.models.Chat
import com.example.bookapp.models.Message
import com.example.bookapp.models.User
import com.example.dataLayer.models.ChatNotificationDTO
import com.example.dataLayer.models.deserialization.FriendRequest
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import com.example.dataLayer.models.serialization.SerializeMessage
import com.example.dataLayer.repositories.ChatRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelChat : ViewModel() {

    @Inject
    lateinit var chatRepository: ChatRepository

    val currentChatId: MutableLiveData<Int> = MutableLiveData()

    @Inject
    lateinit var user: User


    val lastMessageChats: LiveData<List<Int>> by lazy {
        chatRepository.lastChatsMessage
    }


    val userChats: LiveData<List<Chat>> by lazy {
        chatRepository.userChats
    }


    val friendRequests: LiveData<ArrayList<FriendRequest>> by lazy {
        liveData {
            emit(ArrayList(chatRepository.fetchFriendRequests(user)))
        }
    }

    val recentMessages: LiveData<List<Message>> = Transformations.switchMap(currentChatId) {
        currentChatId.value?.let {
            chatRepository.getChatMessages(it)
        }
    }


    val chatLink: LiveData<String?> by lazy {
        chatRepository.chatLink
    }


    fun sendMessage(serializeMessage: SerializeMessage) {
        viewModelScope.launch {
            chatRepository.pushMessage(serializeMessage)
        }
    }

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
