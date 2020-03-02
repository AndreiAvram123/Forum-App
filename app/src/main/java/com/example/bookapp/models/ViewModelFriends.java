package com.example.bookapp.models;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.activities.ApiManager;
import com.example.bookapp.api.FriendsRepository;

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
