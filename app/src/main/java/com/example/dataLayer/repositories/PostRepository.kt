package com.example.dataLayer.repositories

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.Post
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.dataMappers.PostMapper
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.RoomPostDao
import com.example.dataLayer.models.PostDTO
import com.example.dataLayer.models.RoomPostDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@InternalCoroutinesApi
class PostRepository(private val application: Application, private val coroutineScope: CoroutineScope) {

    private var currentPage: Int = 0

    val nextPagePosts: MutableLiveData<ArrayList<Post>>
            by lazy {
        MutableLiveData<ArrayList<Post>>()
    }
    private val posts: MutableLiveData<ArrayList<Post>> by lazy {
        MutableLiveData<ArrayList<Post>>()
    }
    private val favoritePosts: MutableLiveData<ArrayList<Post>> by lazy {
        MutableLiveData<ArrayList<Post>>()
    }

    private val myPosts: MutableLiveData<ArrayList<Post>> by lazy {
        MutableLiveData<ArrayList<Post>>()
    }
    private val repositoryInterface: PostRepositoryInterface by lazy {
        AppUtilities.getRetrofit().create(PostRepositoryInterface::class.java)
    }

    val currentFetchedPost: MutableLiveData<Post> by lazy {
        MutableLiveData<Post>()
    }
    val newFetchedPosts = MutableLiveData<ArrayList<Post>>()
    val postDao: RoomPostDao = PostDatabase.getDatabase(application).postDao()


    fun fetchFirstPagePosts(): MutableLiveData<ArrayList<Post>> {
        repositoryInterface.fetchPostByPage().enqueue(object : Callback<ArrayList<PostDTO>> {
            override fun onResponse(call: Call<ArrayList<PostDTO>?>, response: Response<ArrayList<PostDTO>?>) {
                response.body()?.let {
                    posts.value = PostMapper.convertDTONetworkToDomainObjects(it)
                    currentPage = 1

                   coroutineScope.launch {
                       postDao.addPost(PostMapper.mapDomainToRoomDTO(posts.value!![0]))
                   }
                }

            }
            override fun onFailure(call: Call<ArrayList<PostDTO>?>, t: Throwable) {}
        })
        return posts
    }
     fun fetchAllCachedPosts(): MutableLiveData<ArrayList<Post>> {
        coroutineScope.launch {
            val fetchedData:List<RoomPostDTO>? = postDao.getAllPosts()
            fetchedData?.let{
                posts.value = PostMapper.mapRoomDTOToDomainObjects(fetchedData)
            }
            Log.d("test",fetchedData.toString())
        }
        return posts;
    }

    fun fetchPostByID(id: Long): MutableLiveData<Post> {
        coroutineScope.launch {
            val cachedPost: RoomPostDTO? = postDao.getPostByID(id)
            if (cachedPost == null) {
                repositoryInterface.fetchPostByID(id).enqueue(object : Callback<PostDTO> {
                    override fun onResponse(call: Call<PostDTO>, response: Response<PostDTO>) {
                        val fetchedPost: Post = PostMapper.convertDtoObjectToDomainObject(response.body())
                        currentFetchedPost.value = fetchedPost
                        suspend {
                            postDao.addPost(PostMapper.mapDomainToRoomDTO(fetchedPost))
                        }
                    }

                    override fun onFailure(call: Call<PostDTO>, t: Throwable) {}
                })
            } else {
                currentFetchedPost.postValue(PostMapper.mapRoomDTOToDomainObject(cachedPost))
            }
        }
        return currentFetchedPost
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
                    PostMapper.convertDTONetworkToDomainObjects(it)
                }
            }

            override fun onFailure(call: Call<ArrayList<PostDTO>>, t: Throwable) {}
        })
        return favoritePosts;
    }

    fun fetchMyPosts(userID: String?): MutableLiveData<ArrayList<Post>> {
        repositoryInterface.fetchMyPosts(userID, true).enqueue(object : Callback<ArrayList<PostDTO>> {
            override fun onResponse(call: Call<ArrayList<PostDTO>>, response: Response<ArrayList<PostDTO>>) {
                myPosts.value = response.body()?.let { PostMapper.convertDTONetworkToDomainObjects(it) }
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


    fun fetchNextPagePosts(): MutableLiveData<java.util.ArrayList<Post>> {
        repositoryInterface.fetchPostByPage(currentPage + 1).enqueue(object : Callback<ArrayList<PostDTO>> {
            override fun onResponse(call: Call<ArrayList<PostDTO>>, response: Response<ArrayList<PostDTO>>) {
                response.body()?.let {
                    currentPage++
                    nextPagePosts.value = PostMapper.convertDTONetworkToDomainObjects(it)
                    posts.value?.addAll(nextPagePosts.value!!)
                }

            }

            override fun onFailure(call: Call<ArrayList<PostDTO>>, t: Throwable) {

            }
        })

        return nextPagePosts
    }
    }

