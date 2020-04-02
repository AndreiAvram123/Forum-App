package com.example.dataLayer.repositories

import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.Post
import com.example.dataLayer.dataMappers.PostMapper
import com.example.dataLayer.interfaces.SearchRepositoryInterface
import java.util.*

object SearchRepository {
    private val searchRepositoryInterface: SearchRepositoryInterface = AppUtilities.getRetrofit().create(SearchRepositoryInterface::class.java)
    val searchSuggestions = MutableLiveData<ArrayList<Post>>()

    suspend fun fetchSearchSuggestions(query: String) {
        try {
            val fetchedData = searchRepositoryInterface.fetchSearchSuggestions(query)
            searchSuggestions.value = PostMapper.mapDTONetworkToDomainObjects(fetchedData);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}