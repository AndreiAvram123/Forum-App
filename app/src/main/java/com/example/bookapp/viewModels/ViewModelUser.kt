package com.example.bookapp.viewModels

import android.content.Context
import androidx.lifecycle.*
import com.example.bookapp.models.User
import com.example.dataLayer.models.deserialization.DeserializeFriendRequest
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import com.example.dataLayer.repositories.PostRepository
import com.example.dataLayer.repositories.UserRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelUser : ViewModel() {



    val  userRepository: UserRepository = UserRepository(viewModelScope)

    val searchQuery = MutableLiveData<String>()


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
        viewModelScope.launch {
            userRepository.acceptFriendRequest(request)
        }
    }


}