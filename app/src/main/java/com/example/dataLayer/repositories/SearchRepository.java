package com.example.dataLayer.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.bookapp.models.Post;
import com.example.dataLayer.interfaces.SearchRepositoryInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchRepository {
    private static SearchRepository instance;
    private SearchRepositoryInterface searchRepositoryInterface;
    private MutableLiveData<ArrayList<Post>> searchSuggestions = new MutableLiveData<>();

    private SearchRepository(Retrofit retrofit) {
        searchRepositoryInterface = retrofit.create(SearchRepositoryInterface.class);
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
        searchRepositoryInterface.fetchSearchSuggestions(query).enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Post>> call, @NonNull Response<ArrayList<Post>> response) {
                Log.d("haha",response.body().toString());
                ArrayList<Post> fetchedData = response.body();
                searchSuggestions.setValue(fetchedData);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Post>> call, @NonNull Throwable t) {

            }
        });
        return searchSuggestions;
    }
}
