package com.example.dataLayer.repositories

import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.interfaces.FriendsRepositoryInterface
import com.example.bookapp.models.Friend
import com.example.dataLayer.dataMappers.FriendsMapper
import com.example.dataLayer.models.FriendDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FriendsRepository {
    private val friends: MutableLiveData<ArrayList<Friend>> by lazy {
        MutableLiveData<ArrayList<Friend>>()
    }
    private val repositoryInterface: FriendsRepositoryInterface by lazy {
        AppUtilities.getRetrofit().create(FriendsRepositoryInterface::class.java)
    }


    fun fetchAllFriends(userID: String?): MutableLiveData<ArrayList<Friend>> {
        repositoryInterface.getFriends(userID, friends = true, lastMessage = true).enqueue(object : Callback<ArrayList<FriendDTO>> {
            override fun onResponse(call: Call<ArrayList<FriendDTO>>, response: Response<ArrayList<FriendDTO>>) {
                response.body()?.let {
                    friends.value = FriendsMapper.mapNetworkToDomainObjects(it)
                }
            }
            override fun onFailure(call: Call<ArrayList<FriendDTO>>, t: Throwable) {}
        })
        return friends;
    }

}