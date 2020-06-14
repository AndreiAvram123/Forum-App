package com.example.bookapp.viewModels

import androidx.lifecycle.*
import com.example.bookapp.models.Chat
import com.example.bookapp.models.Message
import com.example.bookapp.models.MessageDTO
import com.example.bookapp.models.User
import com.example.dataLayer.interfaces.ChatLink
import com.example.dataLayer.models.ChatNotificationDTO
import com.example.dataLayer.models.deserialization.FriendRequest
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import com.example.dataLayer.models.serialization.SerializeMessage
import com.example.dataLayer.repositories.ChatRepository
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import javax.inject.Inject

class ViewModelChat : ViewModel() {

    @Inject
    lateinit var chatRepository: ChatRepository

    val chatID: MutableLiveData<Int> = MutableLiveData()

    @Inject
    lateinit var user: User


    val fetchChatNotifications = MutableLiveData<Boolean>()

    val chatNotifications: LiveData<ArrayList<ChatNotificationDTO>> = Transformations.switchMap(fetchChatNotifications) {
        liveData {
            emitSource(chatRepository.fetchChatsNotification(user))

        }
    }

    val userChats: LiveData<List<Chat>> by lazy {
        chatRepository.fetchUserChats(user)
    }


    val friendRequests: LiveData<ArrayList<FriendRequest>> by lazy {
        liveData {
            emit(ArrayList(chatRepository.fetchFriendRequests(user)))
        }
    }

    val recentMessages: LiveData<List<Message>> = Transformations.switchMap(chatID) {
        chatID.value?.let {
            chatRepository.getChatMessages(it)
        }
    }


    val chatLink: LiveData<ChatLink?> = Transformations.switchMap(chatID) {
        chatID.value?.let {
            liveData {
                emit(chatRepository.getChatLink(it))
            }
        }
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
