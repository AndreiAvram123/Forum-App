package com.andrei.kit.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.kit.models.User
import com.andrei.dataLayer.repositories.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch

class ViewModelAuth @ViewModelInject constructor(
        private val userRepository: UserRepository) : ViewModel() {


    val searchQuery = MutableLiveData<String>()

    val authenticationState by lazy {
        userRepository.authenticationError
    }
    val registrationState by lazy {
        userRepository.registrationError
    }

        val searchSuggestions: LiveData<List<User>> = Transformations.switchMap(searchQuery) {
            it?.let {
                return@switchMap userRepository.fetchSearchSuggestions(it)
            }
        }


        fun loginWithGoogle(googleSignInAccount: GoogleSignInAccount) = viewModelScope.launch {  userRepository.loginWithGoogle(googleSignInAccount)}


        fun register(username: String, email: String, password: String)  =  userRepository.registerWithUsernameAndPassword(
                username,email,password)


        fun login(email: String, password: String) = viewModelScope.launch {  userRepository.login(email, password)}

    }

