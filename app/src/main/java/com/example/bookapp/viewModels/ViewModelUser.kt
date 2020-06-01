package com.example.bookapp.viewModels

import androidx.lifecycle.*
import com.example.bookapp.models.User
import com.example.dataLayer.repositories.UserRepository

class ViewModelUser : ViewModel() {


    private val userRepository: UserRepository by lazy {
        UserRepository(viewModelScope)
    }

    val user: MutableLiveData<User> = userRepository.currentFetchedUser


    val friends: LiveData<List<User>> = Transformations.switchMap(user) {
        user.value?.let {
            userRepository.fetchFriends(it)
        }
    }


    fun loginWithGoogle(idToken: String, displayName: String, email: String) {
        userRepository.loginWithGoogle(idToken, displayName, email)
    }


}