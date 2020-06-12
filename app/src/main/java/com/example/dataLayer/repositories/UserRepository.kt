package com.example.dataLayer.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.UserMapper
import com.example.dataLayer.interfaces.UserRepositoryInterface
import com.example.dataLayer.models.deserialization.FriendRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserRepository @Inject constructor(private val coroutineScope: CoroutineScope,
                                         private val user: User) {

    private val currentSearchSuggestions = MutableLiveData<List<User>>()

    private val friendRequests = MutableLiveData<ArrayList<FriendRequest>>()

    private val userRepositoryInterface: UserRepositoryInterface by lazy {
        AppUtilities.getRetrofit().create(UserRepositoryInterface::class.java)
    }


    val friends: LiveData<List<User>> by lazy {
        liveData(Dispatchers.IO) {
            val fetchedData = userRepositoryInterface.fetchFriends(user.userID)
            val friendsDomain = UserMapper.mapDTONetworkToDomainObjects(fetchedData)
            emit(friendsDomain)
        }
    }


    fun loginWithGoogle(idToken: String, displayName: String, email: String): LiveData<User> {
        val liveData = MutableLiveData<User>()
        coroutineScope.launch {
            try {

                val fetchedUser = userRepositoryInterface.fetchGoogleUser(idToken, displayName, email)

                if (fetchedUser.userID == 0) {
                    createGoogleAccount(idToken, displayName, email)
                } else {
                    liveData.postValue(UserMapper.mapToDomainObject(fetchedUser))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return liveData
    }

    private suspend fun createGoogleAccount(idToken: String, displayName: String, email: String): LiveData<User> {
        val liveData = MutableLiveData<User>()
        try {
            val newUser = userRepositoryInterface.createGoogleAccount(idToken, displayName, email)
            liveData.postValue(UserMapper.mapToDomainObject(newUser))

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return liveData
    }

    fun fetchSearchSuggestions(query: String): LiveData<List<User>> {
        //clear previous data
        currentSearchSuggestions.value = ArrayList()
        coroutineScope.launch {
            val fetchedSuggestions = userRepositoryInterface.fetchSuggestions(query)
            currentSearchSuggestions.postValue(UserMapper.mapDTONetworkToDomainObjects(fetchedSuggestions))
        }
        return currentSearchSuggestions;
    }
}