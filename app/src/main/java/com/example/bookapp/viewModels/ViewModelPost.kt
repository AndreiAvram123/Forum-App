package com.example.bookapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.activities.AppUtilities
import com.example.bookapp.models.Post
import com.example.dataLayer.dataObjectsToSerialize.SerializePost
import com.example.dataLayer.repositories.PostRepository
import java.util.*

class ViewModelPost : ViewModel() {
    private var currentFetchedPosts: MutableLiveData<ArrayList<Post>>? = null
    private var currentlyDisplayedPosts: MutableLiveData<ArrayList<Post>?>? = null
    private var myPosts: MutableLiveData<ArrayList<Post>>? = null

        get() = postRepository.newFetchedPosts
    private val postRepository: PostRepository = PostRepository.getInstance(AppUtilities.getRetrofit())
    fun getMyPosts(userID: String?): MutableLiveData<ArrayList<Post>>? {
        if (myPosts == null) { //should fetch posts
            myPosts = postRepository.fetchMyPosts(userID)
        }
        return myPosts
    }

    fun getPost(id: Long): MutableLiveData<Post> {
        return postRepository.fetchPostByID(id)
    }

    fun getFirstPagePosts(page: Int): LiveData<ArrayList<Post>>? {
        if (currentFetchedPosts == null) {
            currentFetchedPosts = postRepository.fetchFirstPagePosts()
        }
        return currentFetchedPosts
    }

    fun getFavoritePosts(userID: String?): MutableLiveData<ArrayList<Post>?>? {
        if (currentlyDisplayedPosts == null) {
            currentlyDisplayedPosts = postRepository.fetchFavoritePosts(userID)
        }
        return currentlyDisplayedPosts
    }

    fun addPostToFavorites(post: Post, userID: String?) {
        postRepository.addPostToFavorites(post.postID, userID)
        //if the favoritePosts data has been fetched you need to update the ui
        if (currentlyDisplayedPosts != null && currentlyDisplayedPosts!!.value != null) {
            val newData = ArrayList(currentlyDisplayedPosts!!.value!!)
            post.isFavorite = true
            newData.add(post)
            currentlyDisplayedPosts!!.value = newData
        }
    }

    fun deletePostFromFavorites(post: Post, userID: String?) {
        postRepository.deletePostFromFavorites(post.postID, userID)
        if (currentlyDisplayedPosts != null && currentlyDisplayedPosts!!.value != null) {
            val newData = ArrayList(currentlyDisplayedPosts!!.value!!)
            newData.remove(post)
            //notify the observers
            currentlyDisplayedPosts!!.value = newData
        }
    }

    fun addPost(serializePost: SerializePost) {
        postRepository.insertPost(serializePost)
    }

    fun fetchNewPosts() {
        postRepository.fetchNewPosts()
    }

}