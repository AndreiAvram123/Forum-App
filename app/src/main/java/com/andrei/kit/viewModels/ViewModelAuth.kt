package com.andrei.kit.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.kit.models.User
import com.andrei.dataLayer.repositories.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch

class ViewModelAuth @ViewModelInject constructor(
        private val authRepository: AuthRepository) : ViewModel() {


    val searchQuery = MutableLiveData<String>()

    val authenticationState by lazy {
        authRepository.authenticationError
    }
    val registrationError by lazy {
        authRepository.registrationError
    }

        val searchSuggestions: LiveData<List<User>> = Transformations.switchMap(searchQuery) {
            it?.let {
                return@switchMap authRepository.fetchSearchSuggestions(it)
            }
        }


        fun loginWithGoogle(googleSignInAccount: GoogleSignInAccount) = viewModelScope.launch {  authRepository.loginWithGoogle(googleSignInAccount)}


        fun register(username: String, email: String, password: String)  =  authRepository.registerWithUsernameAndPassword(
                username,email,password)


        fun login(email: String, password: String) = viewModelScope.launch {  authRepository.login(email, password)}

    }

