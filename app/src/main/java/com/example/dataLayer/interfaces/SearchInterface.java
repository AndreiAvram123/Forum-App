package com.example.dataLayer.interfaces;

import com.example.bookapp.models.Post;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchInterface {

    @GET("RestfulRequestHandler.php")
    Call<ArrayList<Post>> fetchSearchSuggestions(@Query("suggestionQuery") String query);

}
