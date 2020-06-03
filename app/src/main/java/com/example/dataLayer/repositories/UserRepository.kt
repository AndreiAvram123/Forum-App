package com.example.dataLayer.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.UserMapper
import com.example.dataLayer.interfaces.UserRepositoryInterface
import com.example.dataLayer.models.deserialization.DeserializeFriendRequest
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserRepository(private val coroutineScope: CoroutineScope) {
    val currentFetchedUser = MutableLiveData<User>()
    private val currentSearchSuggestions = MutableLiveData<List<User>>()

    private val friendRequests = MutableLiveData<ArrayList<DeserializeFriendRequest>>()

    private val userRepositoryInterface: UserRepositoryInterface by lazy {
        AppUtilities.retrofitGsonConverter.create(UserRepositoryInterface::class.java)
    }

    private var friends: MutableLiveData<ArrayList<User>> = MutableLiveData()


    fun fetchFriends(user: User): LiveData<ArrayList<User>> {
        //clear cache
        friends = MutableLiveData()
        coroutineScope.launch {
            val fetchedData = userRepositoryInterface.fetchFriends(user.userID)
            friends.postValue(ArrayList(UserMapper.mapDTONetworkToDomainObjects(fetchedData)))
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

    fun fetchSearchSuggestions(query: String): LiveData<List<User>> {
        //clear previous data
        currentSearchSuggestions.value = ArrayList()
        coroutineScope.launch {
            val fetchedSuggestions = userRepositoryInterface.fetchSuggestions(query)
            currentSearchSuggestions.postValue(UserMapper.mapDTONetworkToDomainObjects(fetchedSuggestions))
        }
        return currentSearchSuggestions;
    }

    fun sendFriendRequest(friendRequest: SerializeFriendRequest) {
        coroutineScope.launch {
            userRepositoryInterface.pushFriendRequest(friendRequest)
        }
    }

    fun fetchFriendRequests(user: User): LiveData<ArrayList<DeserializeFriendRequest>> {
        friendRequests.value = ArrayList()
        coroutineScope.launch {
            val fetchedData = userRepositoryInterface.fetchFriendRequests(user.userID)
            friendRequests.postValue(ArrayList(fetchedData))
        }
        return friendRequests;
    }

    fun acceptFriendRequest(request: DeserializeFriendRequest) {
        coroutineScope.launch {
            userRepositoryInterface.acceptFriendRequest(request.id)
        }
    }
}