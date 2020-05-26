package com.example.bookapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.dataLayer.models.UserWithPosts
import com.example.dataLayer.repositories.PostRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class ViewModelPost(application: Application) : AndroidViewModel(application) {

    var lastSeenPostPosition: Int = 0;

    val user : MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    private val postRepository: PostRepository by lazy {
        PostRepository(application, coroutineScope = viewModelScope, user = user.value!!)
    }


    fun getUserPosts(): LiveData<UserWithPosts> {
        return postRepository.myPosts
    }

    fun getPostByID(id: Long): LiveData<Post> {
        viewModelScope.launch {
            postRepository.fetchPostByID(id)
        }
        return postRepository.currentFetchedPost
    }

    fun getRecentPosts(): LiveData<List<Post>> {
        return postRepository.fetchedPosts
    }

    fun getFavoritePosts(): LiveData<List<Post>> {
        return postRepository.favoritePosts
    }

    fun addPostToFavorites(post: Post) {
        viewModelScope.launch {
            postRepository.addPostToFavorites(post)
        }
    }

    fun deletePostFromFavorites(post: Post, user: User) =
            viewModelScope.launch { postRepository.deletePostFromFavorites(post) }


    fun fetchNextPagePosts() = viewModelScope.launch { postRepository.fetchNextPagePosts() }


}