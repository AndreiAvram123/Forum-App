package com.example.dataLayer.repositories

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.LowDataPost
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.dataMappers.PostMapper
import com.example.dataLayer.dataMappers.UserMapper
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.dao.RoomPostDao
import com.example.dataLayer.interfaces.dao.RoomUserDao
import com.example.dataLayer.models.*
import com.example.dataLayer.models.serialization.SerializePost
import kotlinx.coroutines.*
import javax.inject.Inject

@InternalCoroutinesApi
class PostRepository @Inject constructor(val connectivityManager: ConnectivityManager,
                                         val postDao: RoomPostDao,
                                         val coroutineScope: CoroutineScope,
                                         val user: User
) {




    val currentSearchResults: MutableLiveData<List<LowDataPost>> = MutableLiveData()
    val currentUploadImagePath: MutableLiveData<String> = MutableLiveData()
    private val currentUploadedPostProgress: MutableLiveData<UploadProgress> = MutableLiveData()

    private var nextPageToFetch: Int = 1;

    private val repositoryInterface: PostRepositoryInterface = AppUtilities.getRetrofit().create(PostRepositoryInterface::class.java)


    val fetchedPosts = liveData(Dispatchers.IO) {
        emitSource(postDao.getCachedPosts())
        if (connectivityManager.isDefaultNetworkActive) {
            fetchNextPagePosts()
        }
    }

    val favoritePosts: LiveData<UserWithFavoritePosts> by lazy {
        liveData(Dispatchers.IO) {
            emitSource(postDao.getFavoritePosts(user.userID))
        }.also {
            if (connectivityManager.isDefaultNetworkActive) {
                coroutineScope.launch { fetchFavoritePosts() }
            }
        }
    }
    val myPosts: LiveData<UserWithPosts> = liveData {
        emitSource(postDao.getAllUserPosts(user.userID))
    }.also {
        if (connectivityManager.isDefaultNetworkActive) {
            coroutineScope.launch {
                fetchMyPosts()
            }
        }
    }


    fun fetchPostByID(id: Int): LiveData<Post> = liveData {
        val postDTO = repositoryInterface.fetchPostByID(id)
        val post = PostMapper.mapDtoObjectToDomainObject(postDTO)
        val author = UserMapper.mapNetworkToDomainObject(postDTO.author);
        postDao.insertPost(post)
        //todo
        //did it break?
        // userDao.insertUser(author)
        emit(post)
    }


    private suspend fun fetchFavoritePosts() {
        val data = PostMapper.mapDTONetworkToDomainObjects(
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
            postDao.insertPosts(PostMapper.mapDTONetworkToDomainObjects(fetchedPosts))
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


    suspend fun fetchNextPagePosts() {
        try {
            if (nextPageToFetch == 1) {
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

    suspend fun fetchSuggestions(query: String) {
        try {
            val fetchedData = repositoryInterface.fetchSearchSuggestions(query)
            currentSearchResults.postValue(fetchedData)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun uploadImage(serializeImage: SerializeImage): LiveData<String> {
        currentUploadImagePath.value = String()
        coroutineScope.launch(Dispatchers.IO) {
            val imagePath = repositoryInterface.uploadImage(serializeImage).message
            currentUploadImagePath.postValue(imagePath)
        }
        return currentUploadImagePath;
    }

    fun uploadPost(post: SerializePost): LiveData<UploadProgress> {
        currentUploadedPostProgress.value = UploadProgress.UPLOADING
        coroutineScope.launch {
            val serverResponse = repositoryInterface.uploadPost(post)

            val fetchedPost = repositoryInterface.fetchPostByID(serverResponse.message.toInt())

            val postDomain = PostMapper.mapDtoObjectToDomainObject(fetchedPost)

            currentUploadedPostProgress.value = UploadProgress.UPLOADED
            postDao.insertPost(postDomain)
        }
        return currentUploadedPostProgress;
    }
}


