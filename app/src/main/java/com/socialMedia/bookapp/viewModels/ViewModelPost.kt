package com.socialMedia.bookapp.viewModels

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.socialMedia.bookapp.models.Post
import com.socialMedia.dataLayer.models.PostDTO
import com.socialMedia.dataLayer.models.SerializeImage
import com.socialMedia.dataLayer.models.UserWithFavoritePosts
import com.socialMedia.dataLayer.repositories.OperationStatus
import com.socialMedia.dataLayer.repositories.PostRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class ViewModelPost @ViewModelInject constructor(
        private val postRepository: PostRepository
) : ViewModel() {


    val favoritePosts: LiveData<UserWithFavoritePosts> by lazy {
        postRepository.favoritePosts
    }


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

    fun uploadPost(postDTO: PostDTO): LiveData<OperationStatus> = postRepository.uploadPost(postDTO)

    fun uploadFirebaseImage(path: Uri): LiveData<String> = postRepository.uploadFirebaseImage(path)


}