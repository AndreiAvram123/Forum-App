package com.example.bookapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.models.Post
import com.example.dataLayer.repositories.SearchRepository
import kotlinx.coroutines.launch
import java.util.*

class ViewModelSearch : ViewModel() {


    fun requestSuggestions(query: String) {
        viewModelScope.launch {
            SearchRepository.fetchSearchSuggestions(query)
        }
    }

    fun getObservableSearchSuggestions()
            : MutableLiveData<ArrayList<Post>> {
        return SearchRepository.searchSuggestions
    }
}