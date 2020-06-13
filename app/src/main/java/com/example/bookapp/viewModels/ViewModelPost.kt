package com.example.bookapp.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.bookapp.models.LowDataPost
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.dataLayer.models.SerializeImage
import com.example.dataLayer.models.UserWithFavoritePosts
import com.example.dataLayer.models.UserWithPosts
import com.example.dataLayer.models.serialization.SerializePost
import com.example.dataLayer.repositories.PostRepository
import com.example.dataLayer.repositories.UploadProgress
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
class ViewModelPost : ViewModel() {


    fun getFavoritePosts(): LiveData<UserWithFavoritePosts> = postRepository.favoritePosts

    @Inject
    lateinit var postRepository: PostRepository


    fun getUserPosts(): LiveData<UserWithPosts> = Transformations.map(postRepository.myPosts) {
        it
    }


    fun getPostByID(id: Int): LiveData<Post> {
        return postRepository.fetchPostByID(id)
    }

    fun getRecentPosts(): LiveData<List<Post>> {
        return postRepository.fetchedPosts
    }


    fun addPostToFavorites(post: Post) {
        viewModelScope.launch {
            postRepository.addPostToFavorites(post)
        }
    }

    fun deletePostFromFavorites(post: Post) =
            viewModelScope.launch { postRepository.deletePostFromFavorites(post) }


    fun fetchNextPagePosts() = viewModelScope.launch { postRepository.fetchNextPagePosts() }

    fun uploadImage(serializeImage: SerializeImage) = postRepository.uploadImage(serializeImage)

    fun uploadPost(post: SerializePost): LiveData<UploadProgress> = postRepository.uploadPost(post)


}