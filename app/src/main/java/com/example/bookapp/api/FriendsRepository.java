package com.example.bookapp.api;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.bookapp.interfaces.FriendsRepositoryInterface;
import com.example.bookapp.models.Friend;
import com.example.bookapp.utilities.FriendsDataConverter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FriendsRepository {
    private static FriendsRepository instance;
    private MutableLiveData<ArrayList<Friend>> friends;
    private FriendsRepositoryInterface repositoryInterface;

    public static FriendsRepository getInstance(@NonNull Retrofit retrofit) {
        if (instance == null) {
            instance = new FriendsRepository(retrofit);
        }
        return instance;
    }

    private FriendsRepository(@NonNull Retrofit retrofit) {
        repositoryInterface = retrofit.create(FriendsRepositoryInterface.class);
    }


    public MutableLiveData<ArrayList<Friend>> fetchAllFriends(String userID) {
        //initialize the data
        friends = new MutableLiveData<>();
        repositoryInterface.getFriends(userID, true, true).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
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
