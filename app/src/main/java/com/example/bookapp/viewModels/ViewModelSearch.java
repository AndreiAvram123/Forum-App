package com.example.bookapp.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.AppUtilities;
import com.example.dataLayer.repositories.SearchRepository;
import com.example.bookapp.models.Post;

import java.util.ArrayList;

public class ViewModelSearch extends ViewModel {
    private SearchRepository searchRepository;
    private MutableLiveData<ArrayList<Post>> searchSuggestions;

    public ViewModelSearch() {
        super();
        searchRepository = SearchRepository.getInstance(AppUtilities.getRetrofit());
    }

    public void requestSuggestions(String query) {
        searchSuggestions = searchRepository.fetchSearchSuggestions(query);
    }

    public MutableLiveData<ArrayList<Post>> getObservableSearchSuggestions() {
        if (searchSuggestions == null) {
            searchSuggestions = searchRepository.getSearchSuggestions();
        }
        return searchSuggestions;
    }
}
