package com.example.bookapp.api;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.bookapp.FriendsDataConverter;
import com.example.bookapp.models.ApiConstants;
import com.example.bookapp.models.Friend;
import com.example.bookapp.models.User;

import java.util.ArrayList;

public class FriendsRepository {
    private RequestQueue requestQueue;
    private static FriendsRepository instance;
    private ApiManagerFriendsDataCallback apiManagerFriendsDataCallback;

    public static FriendsRepository getInstance(RequestQueue requestQueue) {
        if (instance == null) {
            instance = new FriendsRepository(requestQueue);
        }
        return instance;
    }

    private FriendsRepository(@NonNull RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void pushRequestFetchAllFriends(String userID) {
        String formattedURLSavedPosts = String.format(ApiConstants.URL_MY_FRIENDS, userID);
        //push request
        StringRequest request = new StringRequest(Request.Method.GET, formattedURLSavedPosts, (response) ->
        {
            ArrayList<Friend> friends = FriendsDataConverter.convertJsonArrayToFriendsObjects(response);
            apiManagerFriendsDataCallback.onFriendsDataReady(friends);
        },
                Throwable::printStackTrace);
        requestQueue.add(request);
    }

    public void setApiManagerFriendsDataCallback(@NonNull ApiManagerFriendsDataCallback apiManagerFriendsDataCallback) {
        this.apiManagerFriendsDataCallback = apiManagerFriendsDataCallback;
    }

    public interface ApiManagerFriendsDataCallback {
        void onFriendsDataReady(ArrayList<Friend> friends);
    }
}
