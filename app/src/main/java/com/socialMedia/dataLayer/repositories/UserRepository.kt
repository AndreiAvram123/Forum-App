package com.socialMedia.dataLayer.repositories

import androidx.lifecycle.liveData
import com.socialMedia.bookapp.models.User
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class UserRepository @Inject constructor() {


    fun fetchSearchSuggestions(query: String) = liveData {
        emit(listOf<User>())
            try {
//                val fetchedSuggestions = repo.fetchSuggestions(query)
//                emit(fetchedSuggestions.map { it.toUser() })
            } catch (e: Exception) {
                e.printStackTrace()
            }

    }

}