package com.socialMedia.bookapp.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.socialMedia.bookapp.models.User
import com.socialMedia.dataLayer.repositories.UserRepository
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


}