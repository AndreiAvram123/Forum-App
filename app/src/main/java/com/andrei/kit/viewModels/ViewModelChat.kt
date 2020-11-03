package com.andrei.kit.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.dataLayer.engineUtils.Resource
import com.andrei.dataLayer.engineUtils.Status
import com.andrei.kit.models.Chat
import com.andrei.kit.models.Message
import com.andrei.kit.models.User
import com.andrei.dataLayer.models.deserialization.FriendRequest
import com.andrei.dataLayer.models.serialization.SerializeFriendRequest
import com.andrei.dataLayer.models.serialization.SerializeMessage
import com.andrei.dataLayer.repositories.ChatRepository
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
            chatRepository.getCachedMessages(it)
        }
    }




    fun fetchNewMessages (chatID:Int) = chatRepository.fetchNewMessages(chatID)

    val chatLink: LiveData<String> = chatRepository.chatLink


    fun sendMessage(serializeMessage: SerializeMessage) = chatRepository.pushMessage(serializeMessage)

    fun sendFriendRequest(friendRequest: SerializeFriendRequest) {
        viewModelScope.launch {
            chatRepository.sendFriendRequest(friendRequest)
        }
    }

    fun acceptFriendRequest(request: FriendRequest) = liveData{
        emit(Resource.loading<Any>())
        viewModelScope.launch {
            val response  = chatRepository.acceptFriendRequest(request)
            if(response.status == Status.SUCCESS) {
                friendRequests.value?.remove(request)
            }
            emit(response)
        }
    }
}


