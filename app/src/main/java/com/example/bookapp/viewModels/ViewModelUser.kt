package com.example.bookapp.viewModels

import androidx.lifecycle.*
import com.example.bookapp.models.User
import com.example.bookapp.user.UserAccountManager
import com.example.dataLayer.dataMappers.UserMapper
import com.example.dataLayer.repositories.OperationStatus
import com.example.dataLayer.repositories.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelUser : ViewModel() {


    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var user: User

    @Inject
    lateinit var userAccountManager: UserAccountManager

    val searchQuery = MutableLiveData<String>()

    val registrationMessage by lazy {
        MutableLiveData<String>()
    }


    val searchSuggestions: LiveData<List<User>> = Transformations.switchMap(searchQuery) {
        it?.let {
            return@switchMap userRepository.fetchSearchSuggestions(it)
        }
    }


    fun loginWithGoogle(idToken: String, displayName: String, email: String) = userRepository.loginWithGoogle(idToken, displayName, email)


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


    val loginResult = MutableLiveData(OperationStatus.NOT_STARTED)

    fun login(username: String, password: String) {
        loginResult.value = OperationStatus.ONGOING
        viewModelScope.launch {
            val user = userRepository.login(username, password)
            if (user.userID != 0) {
                userAccountManager.saveUserInMemory(UserMapper.mapToDomainObject(user))
            } else {
                loginResult.postValue(OperationStatus.FAILED)
            }

        }
    }

}