package com.example.bookapp.viewModels

import androidx.lifecycle.*
import com.example.bookapp.models.Chat
import com.example.bookapp.models.MessageDTO
import com.example.bookapp.models.User
import com.example.dataLayer.interfaces.ChatLink
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

    val chatID: MutableLiveData<Int> = MutableLiveData()


    val user: MutableLiveData<User> = MutableLiveData()

    val fetchChatNotifications = MutableLiveData<Boolean>()

    val chatNotifications: LiveData<ArrayList<ChatNotificationDTO>> = Transformations.switchMap(fetchChatNotifications) {
        liveData {
            val user = user.value
            if (it && user != null) {
                emit(chatRepository.fetchChatsNotification(user))
            }
        }
    }


    var userChats: LiveData<ArrayList<Chat>> = Transformations.switchMap(user) {
        liveData {
            user.value?.let {
                emitSource(chatRepository.fetchUserChats(it))
            }
        }
    }


    val friendRequests: LiveData<ArrayList<FriendRequest>> = Transformations.switchMap(user) {
        liveData {
            emit(chatRepository.fetchFriendRequests(it))
        }
    }

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


    fun sendMessage(serializeMessage: SerializeMessage) = chatRepository.pushMessage(serializeMessage)

    fun sendFriendRequest(friendRequest: SerializeFriendRequest) {
        chatRepository.sendFriendRequest(friendRequest)
    }

    fun acceptFriendRequest(request: FriendRequest) {
        friendRequests.value?.remove(request)
        viewModelScope.launch {
            chatRepository.acceptFriendRequest(request)
        }
    }

}