package com.andrei.kit.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.andrei.dataLayer.repositories.AuthRepository
import com.andrei.dataLayer.repositories.UserRepository

class ViewModelUser @ViewModelInject constructor(
        private val userRepository: UserRepository
):ViewModel() {
    fun getUser(userID:String) = userRepository.fetchUserDetails(userID)
}