package com.andrei.dataLayer.repositories

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import com.andrei.kit.models.Post
import com.andrei.kit.models.User
import com.andrei.dataLayer.dataMappers.toPost
import com.andrei.dataLayer.engineUtils.*
import com.andrei.dataLayer.interfaces.PostRepositoryInterface
import com.andrei.dataLayer.interfaces.dao.RoomPostDao
import com.andrei.dataLayer.models.*
import com.andrei.dataLayer.models.serialization.SerializeFavoritePostRequest
import com.andrei.dataLayer.models.serialization.SerializePost
import com.andrei.kit.utils.isConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.await
import javax.inject.Inject
import kotlin.Exception


class PostRepository @Inject constructor(private val user: User,
                                         private val coroutineScope: CoroutineScope,
                                         private val repo: PostRepositoryInterface,
                                         private val postDao: RoomPostDao,
                                         private val responseHandler: ResponseHandler,
                                         private val connectivityManager: ConnectivityManager
) {

    private val callRunner =  CallRunner(responseHandler)

    val favoritePosts: LiveData<List<Post>>  = liveData {
        emitSource(postDao.getFavoritePosts())
        if(connectivityManager.isConnected()) {
            fetchFavoritePosts()
        }
    }



    fun getCachedPosts() = postDao.getCachedPosts().also {
        coroutineScope.launch {
            //if network is active remove old data and
            //perform a fresh fetch
            fetchInitialPosts()
            fetchFavoritePosts()

        }
    }


    inner class PostRepoBoundaryCallback : PagedList.BoundaryCallback<Post>() {
        override fun onZeroItemsLoaded() {
            //when no items were loaded from room ,trigger a network call
            coroutineScope.launch {
                fetchInitialPosts()
            }
        }

        override fun onItemAtEndLoaded(itemAtEnd: Post) {
            coroutineScope.launch {
                fetchNextPosts(itemAtEnd.id)
            }
        }
    }



    fun fetchPostByID(id: Int) :LiveData<Post> =  liveData {
        callRunner.makeCall(repo.fetchPostByID(id)){
             postDao.insertPost(it.toPost())
        }
        emitSource(postDao.getPostByID(id))
    }


    private suspend fun fetchFavoritePosts() {
         callRunner.makeCall(repo.fetchUserFavoritePosts(
                userID = user.userID)){
            val transformedData = it.map { postDTO-> postDTO.toPost() }
            transformedData.forEach { post -> post.isFavorite = true }
            postDao.updatePosts(transformedData)
        }
    }

    fun fetchMyPosts() = liveData {
        emitSource(postDao.getAllUserPosts(user.userID))
        callRunner.makeObservableCall(repo.fetchUserPosts(user.userID)){
              postDao.insertPosts(mapDomainData(it))
        }
    }

     fun addPostToFavorites(post: Post) = liveData{
        val requestData = SerializeFavoritePostRequest(postID = post.id,
        userID = user.userID)
        emitSource(callRunner.makeObservableCall(repo.addPostToFavorites(requestData)){
            post.apply {
                bookmarkTimes++
                isFavorite = true
            }
            postDao.updatePost(post)
        })
    }


     fun deletePostFromFavorites(post: Post) = liveData {
       emitSource( callRunner.makeObservableCall(repo.removePostFromFavorites(postID = post.id,userID = user.userID)){
            post.apply {
                isFavorite = false
                bookmarkTimes --
            }
            postDao.updatePost(post)
        })
    }


    suspend fun fetchInitialPosts() = callRunner.makeCall(repo.fetchRecentPosts()){
              postDao.insertPosts(mapDomainData(it))
          }


    private suspend fun checkPostIsBookmarked(post: Post) {
        val dbPost = postDao.getPostByIDSuspend(post.id)
        dbPost?.let {
            post.isFavorite = it.isFavorite
        }
    }
    private suspend fun mapDomainData(dtoPosts:List<PostDTO>):List<Post>{
        val mappedData =  dtoPosts.map { it.toPost() }
        mappedData.forEach { checkPostIsBookmarked(it) }
        return mappedData;
    }



    private suspend fun fetchNextPosts(lastPostID: Int) {
        callRunner.makeCall(repo.fetchNextPagePosts(lastPostID = lastPostID)){
          postDao.insertPosts(mapDomainData(it))
        }
    }


    fun uploadPost(post: SerializePost) = callRunner.makeObservableCall(repo.uploadPost(post)){
        postDao.insertPost(it.toPost())
    }
}




