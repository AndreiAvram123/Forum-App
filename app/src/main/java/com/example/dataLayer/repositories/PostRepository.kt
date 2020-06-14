package com.example.dataLayer.repositories

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.PostMapper
import com.example.dataLayer.dataMappers.UserMapper
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.dao.RoomPostDao
import com.example.dataLayer.models.*
import com.example.dataLayer.models.serialization.SerializePost
import kotlinx.coroutines.*
import javax.inject.Inject

@InternalCoroutinesApi
class PostRepository @Inject constructor(private val connectivityManager: ConnectivityManager,
                                         private val postDao: RoomPostDao,
                                         private val user: User,
                                         private val coroutineScope: CoroutineScope,
                                         private val repositoryInterface: PostRepositoryInterface
) {


    fun getPosts() = postDao.getCachedPosts().also {
        if (connectivityManager.activeNetwork != null) {
            coroutineScope.launch {
                //if network is active remove old data and
                //perform a fresh fetch
                postDao.removeCachedData()
            }
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
            if (connectivityManager.activeNetwork != null) {
                fetchFavoritePosts()
            }
        }
    }
    val myPosts: LiveData<UserWithPosts> = liveData {
        emitSource(postDao.getAllUserPosts(user.userID))
        if (connectivityManager.activeNetwork != null) {
            fetchMyPosts()
        }
    }

    fun fetchPostByID(id: Int): LiveData<Post> = liveData {
        val postDTO = repositoryInterface.fetchPostByID(id)
        val post = PostMapper.mapDtoObjectToDomainObject(postDTO)
        val author = UserMapper.mapToDomainObject(postDTO.author);
        postDao.insertPost(post)

        //todo
        //did it break?
        // userDao.insertUser(author)
        emit(post)
    }


    private suspend fun fetchFavoritePosts() {
        val data = PostMapper.mapToDomainObjects(
                repositoryInterface.fetchUserFavoritePosts(user.userID))

        val toInsert = ArrayList<UserWithFavoritePostsCrossRef>()
        data.forEach {
            toInsert.add(UserWithFavoritePostsCrossRef(postID = it.id, userID = user.userID))
        }
        postDao.insertAllFavoritePosts(toInsert)
    }

    private suspend fun fetchMyPosts() {
        try {
            val fetchedPosts = repositoryInterface.fetchMyPosts(user.userID)
            postDao.insertPosts(PostMapper.mapToDomainObjects(fetchedPosts))
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }
    }

    suspend fun addPostToFavorites(post: Post) {
        postDao.addFavoritePost(UserWithFavoritePostsCrossRef(postID = post.id, userID = user.userID))
        repositoryInterface.addPostToFavorites(post.id, user.userID)
    }


    suspend fun deletePostFromFavorites(post: Post) {

        repositoryInterface.removePostFromFavorites(postID = post.id, userID = user.userID)
        val toRemove = UserWithFavoritePostsCrossRef(postID = post.id, userID = user.userID)
        postDao.deletePostFromFavorites(toRemove)

    }


    suspend fun fetchInitialPosts() {
        if (connectivityManager.activeNetwork != null) {
            try {
                val fetchedData: ArrayList<PostDTO> = repositoryInterface.fetchRecentPosts()
                postDao.insertPosts(PostMapper.mapToDomainObjects(fetchedData))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun fetchNextPosts(lastPostID: Int) {
        if (connectivityManager.activeNetwork != null) {
            try {
              val fetchedData = repositoryInterface.fetchNextPagePosts(lastPostID)
              val posts = PostMapper.mapToDomainObjects(fetchedData)
                postDao.insertPosts(posts)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun uploadImage(serializeImage: SerializeImage): LiveData<String> =
            liveData {
                emit(String())
                val imagePath = repositoryInterface.uploadImage(serializeImage).message
                emit(imagePath)
            }

    fun uploadPost(post: SerializePost): LiveData<UploadProgress> {
        return liveData {
            emit(UploadProgress.UPLOADING)

            val serverResponse = repositoryInterface.uploadPost(post)

            val fetchedPost = repositoryInterface.fetchPostByID(serverResponse.message.toInt())

            val postDomain = PostMapper.mapDtoObjectToDomainObject(fetchedPost)
            emit(UploadProgress.UPLOADED)
            postDao.insertPost(postDomain)
        }
    }
}



