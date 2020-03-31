package com.example.bookapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.bookapp.models.Post
import com.example.dataLayer.repositories.PostRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class ViewModelPost(application: Application) : AndroidViewModel(application) {

    var lastSeenPostPosition: Int = 0;
    lateinit var userID: String;

    private val postRepository: PostRepository by lazy {
        PostRepository(application, coroutineScope = viewModelScope, userID = this.userID)
    }


    fun getMyPosts(): LiveData<List<Post>> {
        return postRepository.myPosts
    }

    fun getPostByID(id: Long): LiveData<Post> {
        viewModelScope.launch {
            postRepository.fetchPostByID(id)
        }
        return postRepository.currentFetchedPost
    }

    fun getFirstPagePosts(): LiveData<List<Post>> {

        return postRepository.recentPosts;
    }

    fun getFavoritePosts(): LiveData<List<Post>> {
        return postRepository.favoritePosts
    }

    fun addPostToFavorites(post: Post, userID: String) {
        viewModelScope.launch {
            postRepository.addPostToFavorites(post, userID)
        }
    }

    fun deletePostFromFavorites(post: Post, userID: String) = viewModelScope.launch { postRepository.deletePostFromFavorites(post, userID) }


    fun fetchNextPagePosts() = viewModelScope.launch { postRepository.fetchNextPagePosts() }


}