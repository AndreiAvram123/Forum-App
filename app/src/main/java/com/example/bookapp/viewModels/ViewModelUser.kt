package com.example.bookapp.viewModels

import androidx.lifecycle.*
import com.example.bookapp.models.User
import com.example.dataLayer.models.UserDTO
import com.example.dataLayer.repositories.UserRepository

class ViewModelUser : ViewModel() {


    private val userRepository: UserRepository by lazy {
        UserRepository(viewModelScope)
    }

    val user: MutableLiveData<User> = MutableLiveData()

    val friends: LiveData<List<User>> = Transformations.switchMap(user) {
        user.value?.let {
            userRepository.fetchFriends(it)
        }
    };


    fun getUserFromThirdPartyEmailAccount(email: String): MutableLiveData<User> {
        // user = UserRepository.authenticateWithThirdPartyEmail(email)
        return user;
    }

    fun createThirdPartyAccount(userDTO: UserDTO): MutableLiveData<User> {
        //    user = UserRepository.createThirdPartyAccount(userDTO)

        return user;
    }


}