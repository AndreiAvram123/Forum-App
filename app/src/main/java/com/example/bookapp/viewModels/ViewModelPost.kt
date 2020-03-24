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
    private lateinit var currentFetchedPosts: MutableLiveData<ArrayList<Post>>
    private lateinit var currentlyDisplayedPosts: MutableLiveData<ArrayList<Post>>
    private lateinit var myPosts: MutableLiveData<ArrayList<Post>>

    @InternalCoroutinesApi
    private val postRepository: PostRepository = PostRepository(application,viewModelScope)

    fun getMyPosts(userID: String?): MutableLiveData<ArrayList<Post>>? {
        if (!this::myPosts.isInitialized) {
            myPosts = postRepository.fetchMyPosts(userID)
        }
        return myPosts
    }

    fun getPostByID(id: Long): MutableLiveData<Post> {
        return postRepository.fetchPostByID(id)
    }

    fun getFirstPagePosts(): LiveData<ArrayList<Post>>? {
        if (!this::currentFetchedPosts.isInitialized) {
            currentFetchedPosts = postRepository.fetchAllCachedPosts()
        }
        return currentFetchedPosts
    }

    fun getFavoritePosts(userID: String?): MutableLiveData<ArrayList<Post>>? {
        if (!this::currentlyDisplayedPosts.isInitialized) {
            currentlyDisplayedPosts = postRepository.fetchFavoritePosts(userID)
        }
        return currentlyDisplayedPosts
    }

    fun addPostToFavorites(post: Post, userID: String?) {
        postRepository.addPostToFavorites(post.postID, userID)
        //if the favoritePosts data has been fetched you need to update the ui
        if (this::currentlyDisplayedPosts.isInitialized) {
            currentlyDisplayedPosts.value?.let {
                val newData = ArrayList(currentlyDisplayedPosts.value!!)
                post.isFavorite = true;
                newData.add(post)
                currentlyDisplayedPosts.value = newData
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
        postRepository.fetchNextPagePosts()
    }

    fun getObservableNextPagePosts(): MutableLiveData<ArrayList<Post>> {
        return postRepository.nextPagePosts
    }

}