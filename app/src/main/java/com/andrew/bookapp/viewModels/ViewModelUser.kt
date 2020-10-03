package com.andrew.bookapp.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrew.bookapp.models.User
import com.andrew.dataLayer.repositories.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch

class ViewModelUser @ViewModelInject constructor(
        private val userRepository: UserRepository) : ViewModel() {


    val searchQuery = MutableLiveData<String>()


    val searchSuggestions: LiveData<List<User>> = Transformations.switchMap(searchQuery) {
        it?.let {
            return@switchMap userRepository.fetchSearchSuggestions(it)
        }
    }


    fun loginWithGoogle(googleSignInAccount: GoogleSignInAccount) = viewModelScope.launch { userRepository.loginWithGoogle(googleSignInAccount) }


    fun register(username: String, email: String, password: String)  = liveData {  emitSource(userRepository.register(username,email,password)) }


    fun login(email: String, password: String) = userRepository.login(email, password)


}