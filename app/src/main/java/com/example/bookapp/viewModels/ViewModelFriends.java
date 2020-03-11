package com.example.bookapp.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.activities.AppUtilities;
import com.example.dataLayer.repositories.FriendsRepository;
import com.example.bookapp.models.Friend;

import java.util.ArrayList;

public class ViewModelFriends extends ViewModel {

    private MutableLiveData<ArrayList<Friend>> friends;
    private FriendsRepository friendsRepository;

    public ViewModelFriends() {
        super();
        friendsRepository = FriendsRepository.getInstance(AppUtilities.getRetrofit(null));
    }

    public MutableLiveData<ArrayList<Friend>> getFriends(String userID) {
        if (friends == null) {
            //push the request
            friends = friendsRepository.fetchAllFriends(userID);

        }
        return friends;
    }


}
