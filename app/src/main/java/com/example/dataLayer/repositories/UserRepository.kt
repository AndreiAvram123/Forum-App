package com.example.dataLayer.repositories

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.UserMapper
import com.example.dataLayer.interfaces.UserRepositoryInterface
import com.example.dataLayer.models.UserDTO
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class UserRepository @Inject constructor(private val coroutineScope: CoroutineScope,
                                         private val repo: UserRepositoryInterface,
                                         private val connectivityManager: ConnectivityManager) {


    fun loginWithGoogle(idToken: String, displayName: String, email: String) = liveData {

        try {
            val fetchedUser = repo.fetchGoogleUser(idToken, displayName, email)
            if (fetchedUser.userID == 0) {
                emit(createGoogleAccount(idToken, displayName, email))
            } else {
                emit(UserMapper.mapToDomainObject(fetchedUser))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun createGoogleAccount(idToken: String, displayName: String, email: String): User {
        return try {
            val newUser = repo.createGoogleAccount(idToken, displayName, email)
            UserMapper.mapToDomainObject(newUser)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            createGoogleAccount(idToken,displayName,email)
        }
    }

    fun fetchSearchSuggestions(query: String) = liveData {
        if (connectivityManager.activeNetwork != null) {
            try {
                val fetchedSuggestions = repo.fetchSuggestions(query)
                emit(fetchedSuggestions.map { UserMapper.mapToDomainObject(it) })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun login(username: String, password: String): UserDTO = repo.login(username, password)

    suspend fun register(username: String, email: String, password: String) = repo.register(username, email, password)
}