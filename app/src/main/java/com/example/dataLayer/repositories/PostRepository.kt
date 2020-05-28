package com.example.dataLayer.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.dataMappers.PostMapper
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.dao.RoomPostDao
import com.example.dataLayer.models.PostDTO
import com.example.dataLayer.models.UserWithPosts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class PostRepository(application: Application, coroutineScope: CoroutineScope, val user:User) {

    private var nextPageToFetch: Int = 1;
    var currentFetchedPost: MutableLiveData<Post> = MutableLiveData()

    private val repositoryInterface: PostRepositoryInterface by lazy {
        AppUtilities.getRetrofit().create(PostRepositoryInterface::class.java)
    }

    private val postDao: RoomPostDao = PostDatabase.getDatabase(application).postDao()

    val fetchedPosts: LiveData<List<Post>> by lazy {
        postDao.getCachedPosts()
    }.also {
        if (AppUtilities.isNetworkAvailable(application)) {
            coroutineScope.launch { fetchNextPagePosts() }
        }
    }

    val favoritePosts: LiveData<List<Post>> by lazy {
        postDao.getFavoritePosts()
    }.also {
        if (AppUtilities.isNetworkAvailable(application)) {
            coroutineScope.launch { fetchFavoritePosts() }
        }
    }
    val myPosts: LiveData<UserWithPosts> by lazy {
        MutableLiveData<UserWithPosts>()
        //postDao.getUserPosts(user.userID)
    }.also {
        if (AppUtilities.isNetworkAvailable(application)) {
            coroutineScope.launch {
            //todo
                //change
                fetchMyPosts()
            }
        }
    }


    suspend fun fetchPostByID(id: Int) {
        currentFetchedPost.value = postDao.getPostByID(id);
        try {
            if (currentFetchedPost.value == null) {
                val fetchedPost = PostMapper.mapDtoObjectToDomainObject(repositoryInterface.fetchPostByID(id))
                currentFetchedPost.value = fetchedPost;
                postDao.insertPost(fetchedPost)
            }
        } catch (e: java.lang.Exception) {
            currentFetchedPost.value = Post.buildNullSafeObject();
            e.printStackTrace()
        }
    }

    private suspend fun fetchFavoritePosts() {
        val data = repositoryInterface.fetchFavoritePostsByUserID(user.userID)
        postDao.insertPosts(PostMapper.mapDTONetworkToDomainObjects(data))
    }

    private suspend fun fetchMyPosts() {
        try {
            val fetchedPosts = PostMapper.mapDTONetworkToDomainObjects(repositoryInterface.fetchMyPosts(user.userID))
            postDao.insertPosts(fetchedPosts)
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
    }
    }

    suspend fun addPostToFavorites(post: Post) {
        postDao.addPostToFavorites(post)
        repositoryInterface.addPostToFavorites(post.id, user.userID)
    }


    suspend fun deletePostFromFavorites(post: Post) {
        repositoryInterface.deletePostFromFavorites(postID = post.id, userID = user.userID);
        postDao.deletePostFromFavorites(post)
    }



    suspend fun fetchNextPagePosts() {
        try {
            if(nextPageToFetch == 1){
                //if we have to fetch the first page then delete the cached posts
                 postDao.removeCachedData()
            }
            val fetchedData: ArrayList<PostDTO> = repositoryInterface.fetchNextPage(nextPageToFetch);
            nextPageToFetch++
            postDao.insertPosts(PostMapper.mapDTONetworkToDomainObjects(fetchedData));
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}

