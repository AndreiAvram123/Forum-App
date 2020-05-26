package com.example.dataLayer.repositories

import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.interfaces.FriendsRepositoryInterface
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.UserMapper
import com.example.dataLayer.models.UserDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FriendsRepository {
    private val friends: MutableLiveData<ArrayList<User>> by lazy {
        MutableLiveData<ArrayList<User>>()
    }
    private val repositoryInterface: FriendsRepositoryInterface by lazy {
        AppUtilities.getRetrofit().create(FriendsRepositoryInterface::class.java)
    }


    fun fetchAllFriends(user:User): MutableLiveData<ArrayList<User>> {
        repositoryInterface.getFriends(user.userID, friends = true, lastMessage = true).enqueue(object : Callback<ArrayList<UserDTO>> {
            override fun onResponse(call: Call<ArrayList<UserDTO>>, response: Response<ArrayList<UserDTO>>) {
                response.body()?.let {
                    //friends.value = UserMapper.(it)
                }
            }
            override fun onFailure(call: Call<ArrayList<UserDTO>>, t: Throwable) {}
        })
        return friends;
    }

}