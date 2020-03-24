package com.example.dataLayer.repositories

import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.Post
import com.example.dataLayer.dataMappers.PostMapper
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.models.PostDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PostRepository {
    private var currentPage: Int = 0

    val nextPagePosts: MutableLiveData<ArrayList<Post>> by lazy {
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

    private val currentFetchedPost: MutableLiveData<Post> by lazy {
        MutableLiveData<Post>()
    }
    val newFetchedPosts = MutableLiveData<ArrayList<Post>>()


    fun fetchFirstPagePosts(): MutableLiveData<ArrayList<Post>> {
        repositoryInterface.fetchPostByPage().enqueue(object : Callback<ArrayList<PostDTO>> {
            override fun onResponse(call: Call<ArrayList<PostDTO>?>, response: Response<ArrayList<PostDTO>?>) {
                response.body()?.let {
                    posts.value = PostMapper.convertDTONetworkToDomainObjects(it)
                    currentPage = 1
                }

            }
            override fun onFailure(call: Call<ArrayList<PostDTO>?>, t: Throwable) {}
        })
        return posts
    }

    fun fetchPostByID(id: Long): MutableLiveData<Post> {
        repositoryInterface.fetchPostByID(id).enqueue(object : Callback<PostDTO> {
            override fun onResponse(call: Call<PostDTO>, response: Response<PostDTO>) {
                currentFetchedPost.value = PostMapper.convertDtoObjectToDomainObject(response.body())
            }

            override fun onFailure(call: Call<PostDTO>, t: Throwable) {}
        })
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

    fun fetchNewPosts() {
        //todo
        //implement

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

