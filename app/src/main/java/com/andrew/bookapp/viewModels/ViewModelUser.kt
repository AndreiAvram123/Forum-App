package com.andrew.bookapp.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrew.bookapp.models.User
import com.andrew.dataLayer.repositories.UserRepository
import kotlinx.coroutines.launch

class ViewModelUser @ViewModelInject constructor(
        private val userRepository: UserRepository) : ViewModel() {


    val searchQuery = MutableLiveData<String>()

    val registrationMessage by lazy {
        MutableLiveData<String>()
    }


    val searchSuggestions: LiveData<List<User>> = Transformations.switchMap(searchQuery) {
        it?.let {
            return@switchMap userRepository.fetchSearchSuggestions(it)
        }
    }


    fun loginWithGoogle(idToken: String, displayName: String, email: String) = viewModelScope.launch { userRepository.loginWithGoogle(idToken, displayName, email) }


    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            val response = userRepository.register(username, email, password)
            if (response.errors != null) {
                registrationMessage.postValue(response.errors[0])
            } else {
                registrationMessage.postValue("Success")
            }
        }
    }


    fun login(username: String, password: String) = userRepository.login(username, password)


}