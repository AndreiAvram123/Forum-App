package com.example.bookapp.viewModels

import androidx.lifecycle.*
import com.example.bookapp.models.User
import com.example.dataLayer.repositories.UserRepository
import javax.inject.Inject

class ViewModelUser : ViewModel() {


    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var user: User

    val searchQuery = MutableLiveData<String>()


    val searchSuggestions: LiveData<List<User>> = Transformations.switchMap(searchQuery) {
        it?.let {
            return@switchMap userRepository.fetchSearchSuggestions(it)
        }
    }


    val friends: LiveData<List<User>> by lazy {
        Transformations.map(userRepository.friends) { it }
    }


    fun loginWithGoogle(idToken: String, displayName: String, email: String) = userRepository.loginWithGoogle(idToken, displayName, email)

}