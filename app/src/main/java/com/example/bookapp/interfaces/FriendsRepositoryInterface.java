package com.example.bookapp.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FriendsRepositoryInterface {

    @GET("RestfulRequestHandler.php")
    Call<String> getFriends(@Query("userID") String userID, @Query("friends") boolean friends, @Query("lastMessage") boolean lastMessage);


}
