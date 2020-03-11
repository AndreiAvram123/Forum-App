package com.example.dataLayer.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchInterface {

    @GET("RestfulRequestHandler.php")
    Call<String> fetchSearchSuggestions(@Query("suggestionQuery") String query);

}
