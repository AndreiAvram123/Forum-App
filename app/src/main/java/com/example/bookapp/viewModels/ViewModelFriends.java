package com.example.bookapp.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.api.FriendsRepository;
import com.example.bookapp.models.Friend;

import java.util.ArrayList;

public class ViewModelFriends extends ViewModel implements FriendsRepository.ApiManagerFriendsDataCallback {
    private final MutableLiveData<ArrayList<Friend>> friends = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Friend>> getFriends() {
        return friends;
    }

    @Override
    public void onFriendsDataReady(ArrayList<Friend> friends) {
        this.friends.setValue(friends);
    }
}
