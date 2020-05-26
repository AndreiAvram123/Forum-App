package com.example.bookapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.models.User
import com.example.dataLayer.models.UserDTO
import com.example.dataLayer.repositories.UserRepository

class ViewModelUser : ViewModel() {
    val user = MutableLiveData<User>()

    val errorResponseCode: MutableLiveData<Int> by lazy {
        UserRepository.responseCode
    }


    fun getUserFromThirdPartyEmailAccount(email: String): MutableLiveData<User> {
       // user = UserRepository.authenticateWithThirdPartyEmail(email)
        return user;
    }

    fun createThirdPartyAccount(userDTO: UserDTO): MutableLiveData<User> {
    //    user = UserRepository.createThirdPartyAccount(userDTO)

        return user;
    }


}