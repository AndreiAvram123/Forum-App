package com.example.bookapp.viewModels

import androidx.lifecycle.*
import com.example.bookapp.models.User
import com.example.dataLayer.models.deserialization.DeserializeFriendRequest
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import com.example.dataLayer.repositories.UserRepository

class ViewModelUser : ViewModel() {


    val searchQuery = MutableLiveData<String>()

    private val userRepository: UserRepository by lazy {
        UserRepository(viewModelScope)
    }


    val searchSuggestions: LiveData<List<User>> = Transformations.switchMap(searchQuery) {
        it?.let {
            return@switchMap userRepository.fetchSearchSuggestions(it)
        }
    }


    val user: MutableLiveData<User> = userRepository.currentFetchedUser


    val friends: LiveData<ArrayList<User>> = Transformations.switchMap(user) {
        user.value?.let {
            userRepository.fetchFriends(it)
        }
    }

    var friendRequests: LiveData<ArrayList<DeserializeFriendRequest>> = Transformations.switchMap(user) {
        user.value?.let {
            userRepository.fetchFriendRequests(it)
        }
    }


    fun loginWithGoogle(idToken: String, displayName: String, email: String) {
        userRepository.loginWithGoogle(idToken, displayName, email)
    }

    fun sendFriendRequest(friendRequest: SerializeFriendRequest) {
        userRepository.sendFriendRequest(friendRequest)
    }

    fun acceptFriendRequest(request: DeserializeFriendRequest) {
        friendRequests.value?.remove(request)
        userRepository.acceptFriendRequest(request)
    }


}