package com.andrei.dataLayer.repositories

import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import com.andrei.dataLayer.engineUtils.Resource
import com.andrei.dataLayer.engineUtils.ResponseHandler
import com.andrei.kit.models.Post
import com.andrei.kit.models.User
import com.andrei.dataLayer.dataMappers.toPost
import com.andrei.dataLayer.interfaces.PostRepositoryInterface
import com.andrei.dataLayer.interfaces.dao.RoomPostDao
import com.andrei.dataLayer.models.*
import com.andrei.dataLayer.models.serialization.SerializeFavoritePostRequest
import com.andrei.dataLayer.models.serialization.SerializePost
import com.andrei.kit.utils.isConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception


class PostRepository @Inject constructor(private val user: User,
                                         private val coroutineScope: CoroutineScope,
                                         private val repo: PostRepositoryInterface,
                                         private val postDao: RoomPostDao,
                                         private val responseHandler: ResponseHandler,
                                         private val connectivityManager: ConnectivityManager
) {


    val favoritePosts: LiveData<List<Post>>  = liveData {
        emitSource(postDao.getFavoritePosts())
        if(connectivityManager.isConnected()) {
            fetchFavoritePosts()
        }
    }


    private  val TAG = PostRepository::class.java.simpleName

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



    fun fetchPostByID(id: Int): LiveData<Resource<Post>> = liveData {
        emit(Resource.loading<Post>())
        try {
            val post = repo.fetchPostByID(postID = id).toPost()
            checkPostIsBookmarked(post)
            postDao.insertPost(post)
            emit(Resource.success(post))
        }catch(e:Exception){
            emit(responseHandler.handleException<Post>(e,"fetch post by id"))
        }
    }


    private suspend fun fetchFavoritePosts() {
        try {
            val data = repo.fetchUserFavoritePosts(userID = user.userID)
                    .map { it.toPost() }
            data.forEach { it.isFavorite = true }
            postDao.updatePosts(data)
        }catch (e:Exception){
            responseHandler.handleException<Any>(e,"Fetch favorite posts")
        }
    }

    fun fetchMyPosts() = liveData {
        emitSource(postDao.getAllUserPosts(user.userID))
        try {
            val fetchedPosts = repo.fetchUserPosts(userID = user.userID)
            postDao.insertPosts(mapDomainData(fetchedPosts))

        } catch (e: java.lang.Exception) {
            e.printStackTrace();
            Log.e(TAG,"Error while fetching my posts")
        }
    }

     fun addPostToFavorites(post: Post) = liveData{
        val requestData = SerializeFavoritePostRequest(postID = post.id,
        userID = user.userID)
        try {
            repo.addPostToFavorites(requestData)
            post.apply {
                bookmarkTimes++
                isFavorite = true
            }
            postDao.updatePost(post)
            emit(Resource.success(Any()))

        }catch(e:Exception){
            emit(responseHandler.handleException<Any>(e,"Add post to Favorites"))
        }
    }


     fun deletePostFromFavorites(post: Post) = liveData {
        try {
            repo.removePostFromFavorites(postID = post.id, userID = user.userID)
            post.apply {
                isFavorite = false
                bookmarkTimes --
            }
            postDao.updatePost(post)
            emit(Resource.success(Any()))
        }catch (e:Exception){
            emit(responseHandler.handleException<Any>(e,"Remove from favorites"))
        }
    }


    suspend fun fetchInitialPosts() {
      try {
          val fetchedData = repo.fetchRecentPosts()
          postDao.insertPosts(mapDomainData(fetchedData))
      }catch (e:Exception){
        responseHandler.handleException<Any>(e,Endpoint.RECENT_POSTS.url)
      }
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



    internal suspend fun fetchNextPosts(lastPostID: Int) {
        try {
            val fetchedData = repo.fetchNextPagePosts(lastPostID = lastPostID)
            postDao.insertPosts(mapDomainData(fetchedData))
        } catch (e: Exception) {
            responseHandler.handleException<Any>(e,Endpoint.RECENT_POSTS.url)
        }
    }


    fun uploadPost(post: SerializePost) =
         liveData {
            emit(Resource.loading<Post>())
            try {
                val returnedPost = repo.uploadPost(uploadPost = post).toPost()
                postDao.insertPost(returnedPost)
                emit(responseHandler.handleSuccess(returnedPost))
            }catch (e:Exception) {
                emit(responseHandler.handleException<Post>(e,"Upload post"))
            }
        }
    }




