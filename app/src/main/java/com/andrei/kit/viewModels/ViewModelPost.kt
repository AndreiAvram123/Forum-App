package com.andrei.kit.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.andrei.dataLayer.engineUtils.Resource
import com.andrei.dataLayer.models.UserWithFavoritePosts
import com.andrei.dataLayer.models.serialization.SerializePost
import com.andrei.dataLayer.repositories.PostRepository
import com.andrei.kit.models.Post
import kotlinx.coroutines.launch

class ViewModelPost @ViewModelInject constructor(
        private val postRepository: PostRepository
) : ViewModel() {


    fun getFavoritePosts(): LiveData<List<Post>> = postRepository.favoritePosts


    val userPosts by lazy {
        postRepository.fetchMyPosts()
    }


    fun getPostByID(id: Int)  = postRepository.fetchPostByID(id)


    private val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(true)
            .setPrefetchDistance(5)
            .build()


    val recentPosts by lazy {
        LivePagedListBuilder(postRepository.getCachedPosts(), config)
                .setBoundaryCallback(postRepository.PostRepoBoundaryCallback())
                .build()
    }

    fun addPostToFavorites(post: Post)  =
            postRepository.addPostToFavorites(post)


    fun deletePostFromFavorites(post: Post) =
          postRepository.deletePostFromFavorites(post)


    fun refreshPostData() = viewModelScope.launch { postRepository.fetchInitialPosts() }


    fun uploadPost(post: SerializePost) = postRepository.uploadPost(post)


}