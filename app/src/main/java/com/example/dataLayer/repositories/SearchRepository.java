package com.example.dataLayer.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.dataLayer.interfaces.SearchInterface;
import com.example.bookapp.models.Post;
import com.example.bookapp.models.PostConverter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchRepository {
    private static SearchRepository instance;
    private SearchInterface searchInterface;
    private MutableLiveData<ArrayList<Post>> searchSuggestions = new MutableLiveData<>();

    private SearchRepository(Retrofit retrofit) {
        searchInterface = retrofit.create(SearchInterface.class);
    }

    public static synchronized SearchRepository getInstance(@NonNull Retrofit retrofit) {
        if (instance == null) {
            instance = new SearchRepository(retrofit);
        }
        return instance;
    }


    public MutableLiveData<ArrayList<Post>> getSearchSuggestions() {
        return searchSuggestions;
    }

    public MutableLiveData<ArrayList<Post>> fetchSearchSuggestions(String query) {
        searchInterface.fetchSearchSuggestions(query).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                ArrayList<Post> fetchedData = PostConverter.getPostsFromJsonArray(response.body());
                searchSuggestions.setValue(fetchedData);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });
        return searchSuggestions;
    }
}
