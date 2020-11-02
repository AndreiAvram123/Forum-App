package com.andrei.dataLayer.repositories

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import com.andrei.dataLayer.engineUtils.Resource
import com.andrei.dataLayer.engineUtils.ResponseHandler
import com.andrei.bookapp.models.Post
import com.andrei.bookapp.models.User
import com.andrei.bookapp.toBase64
import com.andrei.dataLayer.dataMappers.toPost
import com.andrei.dataLayer.interfaces.PostRepositoryInterface
import com.andrei.dataLayer.interfaces.dao.RoomPostDao
import com.andrei.dataLayer.models.*
import com.andrei.dataLayer.models.serialization.SerializeFavoritePostRequest
import com.andrei.dataLayer.models.serialization.SerializePost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception


class PostRepository @Inject constructor(private val user: User,
                                         private val coroutineScope: CoroutineScope,
                                         private val repo: PostRepositoryInterface,
                                         private val postDao: RoomPostDao
) {

    private val responseHandler = ResponseHandler()


    private  val TAG = PostRepository::class.java.simpleName

    fun getPosts() = postDao.getCachedPosts().also {
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

    val favoritePosts: LiveData<UserWithFavoritePosts> by lazy {
        liveData(Dispatchers.IO) {
            emitSource(postDao.getFavoritePosts(user.userID))
            fetchFavoritePosts()
        }
    }


    fun fetchPostByID(id: Int): LiveData<Resource<Post>>  = liveData {
        try {
            val post = repo.fetchPostByID(id).toPost()
            val resourcePost = responseHandler.handleSuccess(post)
            postDao.insertPost(post)
            emit(resourcePost)
        }catch(e:Exception){
            emit(responseHandler.handleException<Post>(e,"fetch post by id"))
        }
    }


    private suspend fun fetchFavoritePosts() {
        val data =
                repo.fetchUserFavoritePosts(user.userID).map { it.toPost() }

        postDao.insertAllFavoritePosts(data.map {
            UserWithFavoritePostsCrossRef(postID = it.id, userID = user.userID)
        })
    }


    fun fetchMyPosts() = liveData {
        emitSource(postDao.getAllUserPosts(user.userID))
        try {
            val fetchedPosts = repo.fetchMyPosts(user.userID)
            postDao.insertPosts(fetchedPosts.map { it.toPost() })
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
            Log.e(TAG,"Error while fetching my posts")
        }
    }

    suspend fun addPostToFavorites(post: Post) {
        val requestData = SerializeFavoritePostRequest(postID = post.id,
        userID = user.userID)
        postDao.addFavoritePost(UserWithFavoritePostsCrossRef(postID = post.id, userID = user.userID))
        repo.addPostToFavorites(requestData)
    }


    suspend fun deletePostFromFavorites(post: Post) {
        repo.removePostFromFavorites(postID = post.id, userID = user.userID)
        val toRemove = UserWithFavoritePostsCrossRef(postID = post.id, userID = user.userID)
        postDao.deletePostFromFavorites(toRemove)

    }


    suspend fun fetchInitialPosts() {
      try {
          val fetchedData = repo.fetchRecentPosts()
          postDao.removeCachedData()
          postDao.insertPosts(fetchedData.map { it.toPost() })
      }catch (e:Exception){
        responseHandler.handleException<Any>(e,Endpoint.RECENT_POSTS.url)
      }
    }



    internal suspend fun fetchNextPosts(lastPostID: Int) {
        try {
            val fetchedData = repo.fetchNextPagePosts(lastPostID)
            postDao.insertPosts(fetchedData.map { it.toPost()})
        } catch (e: Exception) {
            responseHandler.handleException<Any>(e,Endpoint.RECENT_POSTS.url)
        }
    }


    fun uploadImage(drawable: Drawable) = liveData {
                emit(Resource.loading<String>())
                try{
                    val serializeImage = SerializeImage(drawable.toBase64())
                    val imagePath = responseHandler.handleSuccess(repo.uploadImage(serializeImage).message)
                    emit(imagePath)
                }catch (e:java.lang.Exception){
                    emit(responseHandler.handleException<String>(e,"Upload image"))
                }
            }

    fun uploadPost(post: SerializePost) =
         liveData {
            emit(Resource.loading<Post>())
            try {
                val serverResponse = repo.uploadPost(post)
                val fetchedPost = repo.fetchPostByID(serverResponse.message.toInt())
                val postDomain = fetchedPost.toPost()
                postDao.insertPost(postDomain)
                emit(responseHandler.handleSuccess(postDomain))

            }catch (e:Exception) {
                emit(responseHandler.handleException<Post>(e,"Upload post"))
            }
        }
    }




