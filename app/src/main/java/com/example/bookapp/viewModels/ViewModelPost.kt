package com.example.bookapp.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.bookapp.models.Post
import com.example.dataLayer.models.SerializeImage
import com.example.dataLayer.models.UserWithFavoritePosts
import com.example.dataLayer.models.serialization.SerializePost
import com.example.dataLayer.repositories.OperationStatus
import com.example.dataLayer.repositories.PostRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
class ViewModelPost @ViewModelInject constructor(
        private val postRepository: PostRepository
) : ViewModel() {

    
    fun getFavoritePosts(): LiveData<UserWithFavoritePosts> = postRepository.favoritePosts


    val userPosts by lazy {
        postRepository.fetchMyPosts()
    }


    fun getPostByID(id: String): LiveData<Post> {
        return postRepository.fetchPostByID(id)
    }

    private val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(true)
            .setPrefetchDistance(5)
            .build()


    val recentPosts by lazy {
        LivePagedListBuilder(postRepository.getPosts(), config)
                .setBoundaryCallback(postRepository.PostRepoBoundaryCallback())
                .build()
    }

    fun addPostToFavorites(post: Post) {
        viewModelScope.launch {
            postRepository.addPostToFavorites(post)
        }
    }

    fun deletePostFromFavorites(post: Post) =
            viewModelScope.launch { postRepository.deletePostFromFavorites(post) }


    fun fetchNewPosts() = viewModelScope.launch { postRepository.fetchInitialPosts() }

    fun uploadImage(serializeImage: SerializeImage) = postRepository.uploadImage(serializeImage)

    fun uploadPost(post: SerializePost): LiveData<OperationStatus> = postRepository.uploadPost(post)


}