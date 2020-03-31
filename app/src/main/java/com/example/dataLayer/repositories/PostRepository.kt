package com.example.dataLayer.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.Post
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.dataMappers.PostMapper
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.RoomPostDao
import com.example.dataLayer.models.PostDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class PostRepository(application: Application, coroutineScope: CoroutineScope, val userID: String) {

    private var nextPageToFetch: Int = 1;
    var currentFetchedPost: MutableLiveData<Post> = MutableLiveData()

    private val repositoryInterface: PostRepositoryInterface by lazy {
        AppUtilities.getRetrofit().create(PostRepositoryInterface::class.java)
    }

    private val postDao: RoomPostDao = PostDatabase.getDatabase(application).postDao()

    val recentPosts: LiveData<List<Post>> by lazy {
        postDao.getRecentPosts()
    }.also {
        if (AppUtilities.isNetworkAvailable(application)) {
            coroutineScope.launch { fetchPostFirstPage() }
        }
    }

    val favoritePosts: LiveData<List<Post>> by lazy {
        postDao.getFavoritePosts()
    }.also {
        if (AppUtilities.isNetworkAvailable(application)) {
            coroutineScope.launch { fetchFavoritePosts(userID) }
        }
    }
    val myPosts: LiveData<List<Post>> by lazy {
        postDao.getUserPosts(userID)
    }.also {
        if (AppUtilities.isNetworkAvailable(application)) {
            coroutineScope.launch {
                fetchMyPosts()
            }
        }
    }





    suspend fun fetchPostByID(id: Long, userID: String = "") {
        try {
            currentFetchedPost.value = postDao.getPostByID(id);
            if (currentFetchedPost.value == null) {
                val fetchedPost = PostMapper.mapDtoObjectToDomainObject(repositoryInterface.fetchPostByID(id, userID))
                currentFetchedPost.value = fetchedPost;
                postDao.insertPost(fetchedPost)
            }
        } catch (e: java.lang.Exception) {
            currentFetchedPost.value = Post.buildNullSafeObject();
            e.printStackTrace()

        }
    }

    private suspend fun fetchFavoritePosts(userID: String) {
        val data = repositoryInterface.fetchFavoritePostsByUserID(userID)
        postDao.insertPosts(PostMapper.mapDTONetworkToDomainObjects(data))
    }

    private suspend fun fetchMyPosts() {
        try {
            val fetchedPosts = PostMapper.mapDTONetworkToDomainObjects(repositoryInterface.fetchMyPosts(userID))
            postDao.insertPosts(fetchedPosts)
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
    }
    }

    suspend fun addPostToFavorites(post: Post, userID: String) {
        postDao.addPostToFavorites(post)
        repositoryInterface.addPostToFavorites(post.postID, userID)
    }


    suspend fun deletePostFromFavorites(post: Post, userID: String) {
        repositoryInterface.deletePostFromFavorites(post.postID, userID);
        postDao.deletePostFromFavorites(post)
    }


    suspend fun fetchNextPagePosts() {
        try {
            val fetchedData: ArrayList<PostDTO> = repositoryInterface.fetchPostByPage(nextPageToFetch);
            nextPageToFetch++
            postDao.insertPosts(PostMapper.mapDTONetworkToDomainObjects(fetchedData));
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private suspend fun fetchPostFirstPage() {
            postDao.removeOldFetchedData()
            fetchNextPagePosts()

    }
}

