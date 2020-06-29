package com.socialMedia.dataLayer.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import com.socialMedia.bookapp.models.Post
import com.socialMedia.bookapp.models.User
import com.socialMedia.dataLayer.dataMappers.toPost
import com.socialMedia.dataLayer.interfaces.PostRepositoryInterface
import com.socialMedia.dataLayer.interfaces.dao.RoomPostDao
import com.socialMedia.dataLayer.models.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@InternalCoroutinesApi

class PostRepository @Inject constructor(private val user: User,
                                         private val requestExecutor: RequestExecutor,
                                         private val coroutineScope: CoroutineScope,
                                         private val repo: PostRepositoryInterface,
                                         private val firebaseRepo: FirebaseFirestore,
                                         private val firebaseStorage: FirebaseStorage,
                                         private val postDao: RoomPostDao
) {

    private val postCollection = "posts"

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
                //  fetchNextPosts(itemAtEnd.id)
            }
        }
    }

    val favoritePosts: LiveData<UserWithFavoritePosts> by lazy {
        liveData(Dispatchers.IO) {
            emitSource(postDao.getFavoritePosts(user.userID))
            fetchFavoritePosts()
        }
    }


    fun fetchPostByID(id: String): LiveData<Post> = liveData {
        val fetchedData = firebaseRepo.collection(postCollection).document(id).get().await().toObject(PostDTO::class.java)
        fetchedData?.let {
            val post = it.toPost()
            postDao.insertPost(post)
            emit(post)
        }
    }


    private suspend fun fetchFavoritePosts() {

        val data =
                repo.fetchUserFavoritePosts(user.userID).map { it.toPost() }

//        postDao.insertAllFavoritePosts(data.map {
//            UserWithFavoritePostsCrossRef(postID = it.id, userID = user.userID)
//        })
    }


    fun fetchMyPosts() = liveData {
        emitSource(postDao.getAllUserPosts(user.userID))
        try {
            val fetchedPosts = repo.fetchMyPosts(user.userID)
            postDao.insertPosts(fetchedPosts.map { it.toPost() })
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }
    }

    suspend fun addPostToFavorites(post: Post) {

        //     postDao.addFavoritePost(UserWithFavoritePostsCrossRef(postID = post.id, userID = user.userID))
        //     repo.addPostToFavorites(post.id, user.userID)
    }


    suspend fun deletePostFromFavorites(post: Post) {
        // repo.removePostFromFavorites(postID = post.id, userID = user.userID)
        //  val toRemove = UserWithFavoritePostsCrossRef(postID = post.id, userID = user.userID)
        //  postDao.deletePostFromFavorites(toRemove)

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
                            Log.d("test", it.id)
                            it.toObject(PostDTO::class.java).toPost(it.id)
                        }
                        coroutineScope.launch { postDao.insertPosts(data) }
                    }
                }
    }

    internal suspend fun fetchNextPosts(lastPostID: Int) {
        try {
            val fetchedData = repo.fetchNextPagePosts(lastPostID)
            postDao.insertPosts(fetchedData.map { it.toPost() })
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

    fun uploadPost(postDTO: PostDTO): LiveData<OperationStatus> {
        return liveData {
            //mark the operation ongoing
            emit(OperationStatus.ONGOING)
            //upload the post
            val id = firebaseRepo.collection(postCollection).add(postDTO).await().id
            val fetchedData = firebaseRepo.collection(postCollection).document(id).get().await().toObject(PostDTO::class.java)
            fetchedData?.let {
                val post = it.toPost(id)
                postDao.insertPost(post)
                emit(OperationStatus.FINISHED)
            }

        }
    }

    fun uploadFirebaseImage(path: Uri): LiveData<String> = liveData {
        emit("")
        val filePath = "images/posts/${path.lastPathSegment}"
        firebaseStorage.reference.child(filePath).putFile(path).await()
        //fetch the full url
        val fullUrl = firebaseStorage.getReference(filePath).downloadUrl.await().toString()
        emit(fullUrl)
    }
}



