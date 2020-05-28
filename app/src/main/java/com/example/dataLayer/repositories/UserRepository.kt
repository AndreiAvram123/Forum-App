package com.example.dataLayer.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.ApiConstants
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.UserMapper
import com.example.dataLayer.interfaces.UserRepositoryInterface
import com.example.dataLayer.models.UserDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(val coroutineScope: CoroutineScope) {
    private val currentFetchedUser: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    private val userRepositoryInterface: UserRepositoryInterface by lazy {
        AppUtilities.getRetrofit().create(UserRepositoryInterface::class.java)
    }
    val responseCode: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val friends: MutableLiveData<List<User>> = MutableLiveData()


    fun authenticateWithThirdPartyEmail(email: String): MutableLiveData<User> {
        userRepositoryInterface.authenticationWithThirdPartyEmail(true, email).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                //todo
                //check response if body empty
                responseCode.value = ApiConstants.RESPONSE_CODE_ACCOUNT_UNEXISTENT
            }


            override fun onFailure(call: Call<UserDTO>, t: Throwable) {}
        })
        return currentFetchedUser;
    }

    fun createThirdPartyAccount(userDTO: UserDTO): MutableLiveData<User> {
        userRepositoryInterface.createThirdPartyAccount(true, userDTO).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                //todo
                //check response
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {}
        })

        return currentFetchedUser
    }

    fun fetchFriends(user: User): LiveData<List<User>> {
        coroutineScope.launch {
            val fetchedData = userRepositoryInterface.fetchFriends(user.userID)
            friends.postValue(UserMapper.mapDTONetworkToDomainObjects(fetchedData))
        }
        return friends;

    }
}