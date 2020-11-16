package com.andrei.kit.viewModels

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrei.dataLayer.repositories.AuthRepository
import com.andrei.dataLayer.repositories.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class ViewModelUser @ViewModelInject constructor(
        private val userRepository: UserRepository
):ViewModel() {

    fun getUser(userID:String) = userRepository.fetchUserDetails(userID)

    fun changeProfilePicture(imageData:String, firebaseUser: FirebaseUser) =   userRepository.changeProfilePicture(imageData,firebaseUser)

    val friends by lazy {
        userRepository.friends
    }

}