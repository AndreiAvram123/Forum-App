package com.andrei.kit.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.kit.models.Chat
import com.andrei.kit.models.Message
import com.andrei.dataLayer.models.deserialization.FriendRequest
import com.andrei.dataLayer.models.serialization.SerializeFriendRequest
import com.andrei.dataLayer.models.serialization.SerializeMessage
import com.andrei.dataLayer.repositories.ChatRepository
import com.andrei.kit.utils.addAndNotify


class ViewModelChat @ViewModelInject constructor(
        private val chatRepository: ChatRepository
) : ViewModel() {

    val currentChatId: MutableLiveData<Int> = MutableLiveData()


    val userChats: LiveData<List<Chat>> by lazy {
        chatRepository.userChats
    }



    val receivedFriendRequests = chatRepository.receivedFriendRequests

    private val sentFriendRequests = chatRepository.sentFriendRequests

    val allFriendRequests = MediatorLiveData<MutableList<FriendRequest>>().also{
        it.addSource(receivedFriendRequests) {newValue ->
           it.addAndNotify(newData = newValue)
        }
        it.addSource(sentFriendRequests){newValue->
            it.addAndNotify(newData = newValue)
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

    fun sendFriendRequest(friendRequest: SerializeFriendRequest)  = chatRepository.sendFriendRequest(friendRequest)


    fun acceptFriendRequest(request: FriendRequest) = chatRepository.acceptFriendRequest(request)
}


