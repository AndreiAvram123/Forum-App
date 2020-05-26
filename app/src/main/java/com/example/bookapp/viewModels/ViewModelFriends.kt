package com.example.bookapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.models.User
import com.example.dataLayer.repositories.FriendsRepository
import java.util.*

class ViewModelFriends : ViewModel() {
    lateinit var friends: MutableLiveData<ArrayList<User>>
    private val friendsRepository: FriendsRepository by lazy {
        FriendsRepository
    }

    fun getFriends(user:User): MutableLiveData<ArrayList<User>> {
        //push the request
        if (!this::friends.isInitialized) {
            friends = friendsRepository.fetchAllFriends(user)
        }
        return friends
    }


}