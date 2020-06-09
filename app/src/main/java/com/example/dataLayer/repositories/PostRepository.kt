package com.example.dataLayer.repositories

import android.app.Application
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

@InternalCoroutinesApi
class PostRepository(application: Application, val coroutineScope: CoroutineScope, val user: User) {

    val currentSearchResults: MutableLiveData<List<LowDataPost>> = MutableLiveData()
    val currentUploadImagePath: MutableLiveData<String> = MutableLiveData()
    private val currentUploadedPostProgress: MutableLiveData<UploadProgress> = MutableLiveData()

    private var nextPageToFetch: Int = 1;
    //private val currentFetchedPosts: HashMap<Int, LiveData<Post>> = HashMap()

    private val repositoryInterface: PostRepositoryInterface = AppUtilities.getRetrofit().create(PostRepositoryInterface::class.java)

    private val postDao: RoomPostDao = PostDatabase.getDatabase(application).postDao()

    private val userDao: RoomUserDao = PostDatabase.getDatabase(application).userDao().also {
        coroutineScope.launch {
            it.insertUser(user)
        }
    }

    val fetchedPosts = liveData(Dispatchers.IO) {
        emitSource(postDao.getCachedPosts())
        if (AppUtilities.isNetworkAvailable(application)) {
            fetchNextPagePosts()
        }
    }

    val favoritePosts: LiveData<UserWithFavoritePosts> by lazy {
        liveData(Dispatchers.IO) {
            emitSource(postDao.getFavoritePosts(user.userID))
        }.also {
            if (AppUtilities.isNetworkAvailable(application)) {
                coroutineScope.launch { fetchFavoritePosts() }
            }
        }
    }
    val myPosts: LiveData<UserWithPosts> = liveData {
        emitSource(postDao.getAllUserPosts(user.userID))
    }.also {
        if (AppUtilities.isNetworkAvailable(application)) {
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
        userDao.insertUser(author)
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
        //todo
        //remove
        //   repositoryInterface.removePostFromFavorites(postID = post.id, userID = user.userID);
        postDao.deletePostFromFavorites(post)
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


