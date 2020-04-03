package com.example.dataLayer.repositories

import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.UserMapper
import com.example.dataLayer.interfaces.UserRepositoryInterface
import com.example.dataLayer.models.UserDTO

object UserRepository {
    val currentFetchedUser: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    private val userRepositoryInterface: UserRepositoryInterface by lazy {
        AppUtilities.getRetrofit().create(UserRepositoryInterface::class.java)
    }



    suspend fun authenticateWithThirdPartyAccount(userDTO: UserDTO) {
        try {
            val fetchedUser = UserMapper.mapNetworkToDomainObject(userRepositoryInterface.authenticationWithThirdPartyEmail(userDTO.email))
            if (fetchedUser.userID == "-1") {
                createThirdPartyAccount(userDTO)
            } else {
                currentFetchedUser.postValue(fetchedUser)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun createThirdPartyAccount(userDTO: UserDTO) {
        try {
            val fetchedUser = UserMapper.mapNetworkToDomainObject(userRepositoryInterface.createThirdPartyAccount(userDTO))
            currentFetchedUser.postValue(fetchedUser)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}