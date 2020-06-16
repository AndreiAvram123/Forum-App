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
import com.example.dataLayer.repositories.RequestExecutor.DeferredFunction
import kotlin.collections.ArrayList

@Suppress("MemberVisibilityCanBePrivate")
@InternalCoroutinesApi

class PostRepository @Inject constructor(private val user: User,
                                         private val requestExecutor: RequestExecutor,
                                         private val coroutineScope: CoroutineScope,
                                         private val repo: PostRepositoryInterface,
                                         private val postDao: RoomPostDao
) {

    fun getPosts() = postDao.getCachedPosts().also {
        coroutineScope.launch {
            //if network is active remove old data and
            //perform a fresh fetch
            fetchInitialPosts()
            requestExecutor.executeSuspend(DeferredFunction(this@PostRepository::fetchFavoritePostsImpl))

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
            requestExecutor.executeSuspend(DeferredFunction(this@PostRepository::fetchFavoritePostsImpl))
        }
    }

    val myPosts: LiveData<UserWithPosts> = liveData {
        emitSource(postDao.getAllUserPosts(user.userID))
        fetchMyPosts()
    }

    fun fetchPostByID(id: Int): LiveData<Post> = liveData {
        val postDTO = repo.fetchPostByID(id)
        val post = PostMapper.mapDtoObjectToDomainObject(postDTO)
        val author = UserMapper.mapToDomainObject(postDTO.author);
        postDao.insertPost(post)

        //todo
        //did it break?
        // userDao.insertUser(author)
        emit(post)
    }


    suspend fun fetchFavoritePostsImpl() {
        val data = PostMapper.mapToDomainObjects(
                repo.fetchUserFavoritePosts(user.userID))

        val toInsert = ArrayList<UserWithFavoritePostsCrossRef>()
        data.forEach {
            toInsert.add(UserWithFavoritePostsCrossRef(postID = it.id, userID = user.userID))
        }
        postDao.insertAllFavoritePosts(toInsert)
    }


    suspend fun fetchMyPosts() {
        try {
            val fetchedPosts = repo.fetchMyPosts(user.userID)
            postDao.insertPosts(PostMapper.mapToDomainObjects(fetchedPosts))
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }
    }

    suspend fun addPostToFavorites(post: Post) {
        postDao.addFavoritePost(UserWithFavoritePostsCrossRef(postID = post.id, userID = user.userID))
        repo.addPostToFavorites(post.id, user.userID)
    }


    suspend fun deletePostFromFavorites(post: Post) {

        repo.removePostFromFavorites(postID = post.id, userID = user.userID)
        val toRemove = UserWithFavoritePostsCrossRef(postID = post.id, userID = user.userID)
        postDao.deletePostFromFavorites(toRemove)

    }


    suspend fun fetchInitialPosts() =
            requestExecutor.executeSuspend(DeferredFunction(
                    this::fetchInitialPostsImpl))


    internal suspend fun fetchInitialPostsImpl() {
        postDao.removeCachedData()
        val fetchedData: ArrayList<PostDTO> = repo.fetchRecentPosts()
        postDao.insertPosts(PostMapper.mapToDomainObjects(fetchedData))

    }


    internal suspend fun fetchNextPosts(lastPostID: Int) {
        try {
            val fetchedData = repo.fetchNextPagePosts(lastPostID)
            val posts = PostMapper.mapToDomainObjects(fetchedData)
            postDao.insertPosts(posts)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun uploadImage(serializeImage: SerializeImage): LiveData<String> =
            liveData {
                emit(String())
                val imagePath = repo.uploadImage(serializeImage).message
                emit(imagePath)
            }

    fun uploadPost(post: SerializePost): LiveData<UploadProgress> {
        return liveData {
            emit(UploadProgress.UPLOADING)

            val serverResponse = repo.uploadPost(post)

            val fetchedPost = repo.fetchPostByID(serverResponse.message.toInt())

            val postDomain = PostMapper.mapDtoObjectToDomainObject(fetchedPost)
            emit(UploadProgress.UPLOADED)
            postDao.insertPost(postDomain)
        }
    }
}



