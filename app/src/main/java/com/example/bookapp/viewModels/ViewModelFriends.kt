package com.example.bookapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.models.Friend
import com.example.dataLayer.repositories.FriendsRepository
import java.util.*

class ViewModelFriends : ViewModel() {
    lateinit var friends: MutableLiveData<ArrayList<Friend>>
    private val friendsRepository: FriendsRepository by lazy {
        FriendsRepository
    }

    fun getFriends(userID: String?): MutableLiveData<ArrayList<Friend>> {
        //push the request
        if (!this::friends.isInitialized) {
            friends = friendsRepository.fetchAllFriends(userID)
        }
        return friends
    }


}