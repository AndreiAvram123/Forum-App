package com.example.dataLayer.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.dataLayer.dataMappers.PostMapper
import com.example.dataLayer.dataMappers.toPost
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.dao.RoomPostDao
import com.example.dataLayer.models.*
import com.example.dataLayer.models.serialization.SerializePost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@Suppress("MemberVisibilityCanBePrivate")
@InternalCoroutinesApi

class PostRepository @Inject constructor(private val user: User,
                                         private val requestExecutor: RequestExecutor,
                                         private val coroutineScope: CoroutineScope,
                                         private val repo: PostRepositoryInterface,
                                         private val firebaseRepo: FirebaseFirestore,
                                         private val postDao: RoomPostDao
) {

    private val postCollection = "posts"

    fun getPosts() = postDao.getCachedPosts().also {
        coroutineScope.launch {
            //if network is active remove old data and
            //perform a fresh fetch
            fetchInitialPosts()

            requestExecutor.add(this@PostRepository::fetchFavoritePostsImpl, null)

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
            requestExecutor.add(this@PostRepository::fetchFavoritePostsImpl, null)
        }
    }


    fun fetchPostByID(id: Int): LiveData<Post> = liveData {
        val postDTO = repo.fetchPostByID(id)
        val post = PostMapper.mapToDomainObject(postDTO)
        postDao.insertPost(post)
        emit(post)
    }


    internal suspend fun fetchFavoritePostsImpl() {
        val data =
                repo.fetchUserFavoritePosts(user.userID).map { PostMapper.mapToDomainObject(it) }

        postDao.insertAllFavoritePosts(data.map {
            UserWithFavoritePostsCrossRef(postID = it.id, userID = user.userID)
        })
    }


    fun fetchMyPosts() = liveData {
        emitSource(postDao.getAllUserPosts(user.userID))
        try {
            val fetchedPosts = repo.fetchMyPosts(user.userID)
            postDao.insertPosts(fetchedPosts.map { PostMapper.mapToDomainObject(it) })
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


    suspend fun fetchInitialPosts() {
        postDao.removeCachedData()
        firebaseRepo.collection(postCollection).orderBy(Post::date.name, Query.Direction.DESCENDING).limit(10)
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    snapshots?.let {
                        val data = snapshots.mapNotNull {
                            it.toObject(PostDTO::class.java).toPost()
                        }
                        coroutineScope.launch { postDao.insertPosts(data) }
                    }
                }
    }

    internal suspend fun fetchNextPosts(lastPostID: Int) {
        try {
            val fetchedData = repo.fetchNextPagePosts(lastPostID)
            postDao.insertPosts(fetchedData.map { PostMapper.mapToDomainObject(it) })
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

    fun uploadPost(post: SerializePost): LiveData<OperationStatus> {
        return liveData {
            emit(OperationStatus.ONGOING)

            val serverResponse = repo.uploadPost(post)

            val fetchedPost = repo.fetchPostByID(serverResponse.message.toInt())

            val postDomain = PostMapper.mapToDomainObject(fetchedPost)
            emit(OperationStatus.FINISHED)
            postDao.insertPost(postDomain)
        }
    }
}



