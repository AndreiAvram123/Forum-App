package com.example.dataLayer.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.Post
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.dataMappers.PostMapper
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.RoomPostDao
import com.example.dataLayer.models.PostDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@InternalCoroutinesApi
class PostRepository(private val application: Application) {

    private var currentPage: Int = 0

    val nextPagePosts: MutableLiveData<ArrayList<Post>>
            by lazy {
        MutableLiveData<ArrayList<Post>>()
    }

    val posts: MutableLiveData<List<Post>> by lazy {
        MutableLiveData<List<Post>>()
    }

    var currentFetchedPost: LiveData<Post> = MutableLiveData()

    private val favoritePosts: MutableLiveData<ArrayList<Post>> by lazy {
        MutableLiveData<ArrayList<Post>>()
    }

    private val myPosts: MutableLiveData<ArrayList<Post>> by lazy {
        MutableLiveData<ArrayList<Post>>()
    }
    private val repositoryInterface: PostRepositoryInterface by lazy {
        AppUtilities.getRetrofit().create(PostRepositoryInterface::class.java)
    }

    val newFetchedPosts = MutableLiveData<ArrayList<Post>>()
    val postDao: RoomPostDao = PostDatabase.getDatabase(application).postDao()


    suspend fun fetchFirstPagePosts(): LiveData<List<Post>> {

        withContext(Dispatchers.IO) {
            posts.postValue(postDao.getAllPosts())
        }
        //check weather we have the latest posts
        //fetchFirstsPageByNetwork()
        return posts;
    }

    private suspend fun fetchFirstsPageByNetwork() {
        var fetchedPosts: List<Post>? = null
        withContext(Dispatchers.IO) {
            repositoryInterface.fetchPostByPage().enqueue(object : Callback<ArrayList<PostDTO>> {
                override fun onResponse(call: Call<ArrayList<PostDTO>?>, response: Response<ArrayList<PostDTO>?>) {
                    response.body()?.let {
                        currentPage = 1
                        fetchedPosts = PostMapper.mapDTONetworkToDomainObjects(it)
                    }

                }

                override fun onFailure(call: Call<ArrayList<PostDTO>?>, t: Throwable) {}
            })
        }
        withContext(Dispatchers.IO) {
            fetchedPosts?.let { postDao.insertPosts(it) }
        }
    }


    suspend fun fetchPostByID(id: Long): LiveData<Post> {
        withContext(Dispatchers.IO) {
            currentFetchedPost = postDao.getPostByID(id)
        }

        if (currentFetchedPost.value == null) {
            withContext(Dispatchers.IO) {
                repositoryInterface.fetchPostByID(id).enqueue(object : Callback<PostDTO> {
                    override fun onResponse(call: Call<PostDTO>, response: Response<PostDTO>) {
                        PostMapper.convertDtoObjectToDomainObject(response.body())
                    }

                    override fun onFailure(call: Call<PostDTO>, t: Throwable) {}
                })
            }
        }

        return currentFetchedPost;
    }


    /**
     * This method should be called when the favorite posts
     * data is requested
     * The method decided weather it should fetch the data from
     * cache or from the source
     *
     * @param userID
     * @return
     */
    fun fetchFavoritePosts(userID: String?): MutableLiveData<ArrayList<Post>> {
        //start fetching the other data on the other thread
        repositoryInterface.fetchFavoritePostsByUserID(userID, true).enqueue(object : Callback<ArrayList<PostDTO>> {
            override fun onResponse(call: Call<ArrayList<PostDTO>>, response: Response<ArrayList<PostDTO>>) {
                favoritePosts.value = response.body()?.let {
                    PostMapper.mapDTONetworkToDomainObjects(it)
                }
            }

            override fun onFailure(call: Call<ArrayList<PostDTO>>, t: Throwable) {}
        })
        return favoritePosts;
    }

    fun fetchMyPosts(userID: String?): MutableLiveData<ArrayList<Post>> {
        repositoryInterface.fetchMyPosts(userID, true).enqueue(object : Callback<ArrayList<PostDTO>> {
            override fun onResponse(call: Call<ArrayList<PostDTO>>, response: Response<ArrayList<PostDTO>>) {
                myPosts.value = response.body()?.let { PostMapper.mapDTONetworkToDomainObjects(it) }
            }

            override fun onFailure(call: Call<ArrayList<PostDTO>>, t: Throwable) {}
        })
        return myPosts
    }

    fun addPostToFavorites(postID: Long, userID: String?) {
        repositoryInterface.addPostToFavorites(true, postID, userID).enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {}
            override fun onFailure(call: Call<String?>, t: Throwable) {}
        })
    }


    fun deletePostFromFavorites(postID: Long, userID: String?) {
        //  repositoryInterface.deletePostFromFavorites(postID,userID);
    }


    suspend fun fetchNextPagePosts(): MutableLiveData<java.util.ArrayList<Post>> {
        withContext(Dispatchers.IO) {
            repositoryInterface.fetchPostByPage(currentPage + 1).enqueue(object : Callback<ArrayList<PostDTO>> {
                override fun onResponse(call: Call<ArrayList<PostDTO>>, response: Response<ArrayList<PostDTO>>) {
                    response.body()?.let {
                        currentPage++
                        nextPagePosts.value = PostMapper.mapDTONetworkToDomainObjects(it)
                        // posts.value?.addAll(nextPagePosts.value!!)
                    }

                }

                override fun onFailure(call: Call<ArrayList<PostDTO>>, t: Throwable) {

                }
            })
        }
        return nextPagePosts
    }
    }

