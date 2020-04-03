package com.example.bookapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.models.User
import com.example.dataLayer.models.UserDTO
import com.example.dataLayer.repositories.UserRepository
import kotlinx.coroutines.launch

class ViewModelUser : ViewModel() {
    var user = UserRepository.currentFetchedUser

    fun setUser(user: User) {
        this.user.value = user
    }

    fun getUserFromThirdPartyEmailAccount(userDTO: UserDTO): MutableLiveData<User> {

        viewModelScope.launch {
            UserRepository.authenticateWithThirdPartyAccount(userDTO)
        }
        return user;
    }



}