package com.example.dataLayer.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.UserMapper
import com.example.dataLayer.interfaces.UserRepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserRepository(private val coroutineScope: CoroutineScope) {
    val currentFetchedUser = MutableLiveData<User>()

    private val userRepositoryInterface: UserRepositoryInterface by lazy {
        AppUtilities.retrofit.create(UserRepositoryInterface::class.java)
    }
//    private val repoStringConverter: UserRepositoryInterface by lazy {
//        Retrofit.Builder()
//                .baseUrl("http://www.andreiram.co.uk/")
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build()
//                .create(UserRepositoryInterface::class.java)
//    }

    val friends: MutableLiveData<List<User>> = MutableLiveData()


    fun fetchFriends(user: User): LiveData<List<User>> {
        coroutineScope.launch {
            val fetchedData = userRepositoryInterface.fetchFriends(user.userID)
            friends.postValue(UserMapper.mapDTONetworkToDomainObjects(fetchedData))
        }
        return friends;

    }

    fun loginWithGoogle(idToken: String, displayName: String, email: String) {
        coroutineScope.launch {
            try {

                val fetchedUser = userRepositoryInterface.fetchGoogleUser(idToken, displayName, email)

                if (fetchedUser.userID == 0) {
                    createGoogleAccount(idToken, displayName, email)
                } else {
                    currentFetchedUser.postValue(UserMapper.mapNetworkToDomainObject(fetchedUser))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun createGoogleAccount(idToken: String, displayName: String, email: String) {
        try {
            val newUser = userRepositoryInterface.createGoogleAccount(idToken, displayName, email)
            currentFetchedUser.postValue(UserMapper.mapNetworkToDomainObject(newUser))

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}