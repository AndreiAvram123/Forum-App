package com.example.bookapp.viewModels

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.bookapp.models.Post
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


    @Inject
    lateinit var postRepository: PostRepository

    fun getFavoritePosts(): LiveData<UserWithFavoritePosts> = postRepository.favoritePosts


    fun getUserPosts(): LiveData<UserWithPosts> = Transformations.map(postRepository.myPosts) {
        it
    }


    fun getPostByID(id: Int): LiveData<Post> {
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


    fun fetchNextPagePosts() = viewModelScope.launch { postRepository.fetchInitialPosts() }

    fun uploadImage(serializeImage: SerializeImage) = postRepository.uploadImage(serializeImage)

    fun uploadPost(post: SerializePost): LiveData<UploadProgress> = postRepository.uploadPost(post)


}