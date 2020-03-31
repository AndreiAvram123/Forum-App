package com.example.bookapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookapp.models.Post
import com.example.dataLayer.repositories.PostRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class ViewModelPost(application: Application) : AndroidViewModel(application) {

    private lateinit var currentlyDisplayedPosts: MutableLiveData<ArrayList<Post>>
    private lateinit var myPosts: MutableLiveData<ArrayList<Post>>
    @InternalCoroutinesApi
    private val postRepository: PostRepository = PostRepository(application)

    fun getMyPosts(userID: String?): MutableLiveData<ArrayList<Post>>? {
        if (!this::myPosts.isInitialized) {
            myPosts = postRepository.fetchMyPosts(userID)
        }
        return myPosts
    }

    fun getPostByID(id: Long): LiveData<Post> {
        viewModelScope.launch {
            postRepository.fetchPostByID(id)
        }
        return postRepository.currentFetchedPost
    }

    fun getFirstPagePosts(): LiveData<List<Post>> {

        viewModelScope.launch {
            postRepository.fetchPostFirstPage()
        }
        return postRepository.recentPosts;
    }

    fun getFavoritePosts(userID: String?): LiveData<ArrayList<Post>>? {
        if (!this::currentlyDisplayedPosts.isInitialized) {
            currentlyDisplayedPosts = postRepository.fetchFavoritePosts(userID)
        }
        return currentlyDisplayedPosts
    }

    fun addPostToFavorites(post: Post, userID: String) {
        viewModelScope.launch {
            postRepository.addPostToFavorites(post.postID, userID)
            //if the favoritePosts data has been fetched you need to update the ui
            currentlyDisplayedPosts.value?.let {
                val newData = ArrayList(currentlyDisplayedPosts.value!!)
                newData.add(post)
                currentlyDisplayedPosts.postValue(newData)
            }
        }
    }

    fun deletePostFromFavorites(post: Post, userID: String?) {
        postRepository.deletePostFromFavorites(post.postID, userID)
        currentlyDisplayedPosts.value?.let {
            val newData = ArrayList(currentlyDisplayedPosts.value!!)
            newData.remove(post)
            //notify the observers
            currentlyDisplayedPosts.value = newData
        }
    }

    fun fetchNextPagePosts() {
        viewModelScope.launch {
            postRepository.fetchNextPagePosts()
        }
    }


}