package com.example.dataLayer.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.bookapp.AppUtilities;
import com.example.bookapp.interfaces.FriendsRepositoryInterface;
import com.example.bookapp.models.Friend;
import com.example.bookapp.utilities.FriendsDataConverter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsRepository {
    private static FriendsRepository instance;
    private MutableLiveData<ArrayList<Friend>> friends;
    private FriendsRepositoryInterface repositoryInterface;

    public static synchronized FriendsRepository getInstance() {
        if (instance == null) {
            instance = new FriendsRepository();
        }
        return instance;
    }

    private FriendsRepository() {
        repositoryInterface = AppUtilities.getRetrofit().create(FriendsRepositoryInterface.class);
    }


    public MutableLiveData<ArrayList<Friend>> fetchAllFriends(String userID) {
        //initialize the data
        friends = new MutableLiveData<>();
        repositoryInterface.getFriends(userID, true, true).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                //todo
                //use GSON mapper
                if (response.body() != null) {
                    friends.setValue(FriendsDataConverter.convertJsonArrayToFriendsObjects(response.body()));
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });
        //
        return friends;

    }

}
