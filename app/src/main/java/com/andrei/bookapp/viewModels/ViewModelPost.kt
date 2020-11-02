package com.andrei.bookapp.viewModels

import android.graphics.drawable.Drawable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.andrei.dataLayer.engineUtils.Resource
import com.andrei.bookapp.models.Post
import com.andrei.dataLayer.models.UserWithFavoritePosts
import com.andrei.dataLayer.models.serialization.SerializePost
import com.andrei.dataLayer.repositories.PostRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class ViewModelPost @ViewModelInject constructor(
        private val postRepository: PostRepository
) : ViewModel() {

    
    fun getFavoritePosts(): LiveData<UserWithFavoritePosts> = postRepository.favoritePosts


    val userPosts by lazy {
        postRepository.fetchMyPosts()
    }


    fun getPostByID(id: Int): LiveData<Resource<Post>> = postRepository.fetchPostByID(id)


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

    fun uploadImage(drawable: Drawable) = postRepository.uploadImage(drawable)

    fun uploadPost(post: SerializePost) = postRepository.uploadPost(post)


}