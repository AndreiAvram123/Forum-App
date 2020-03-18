package com.example.dataLayer.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.bookapp.models.Post;
import com.example.dataLayer.dataObjectsToSerialize.SerializePost;
import com.example.dataLayer.interfaces.PostRepositoryInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostRepository {

    //these fields should only represent caching data

    private static PostRepository instance;
    private MutableLiveData<ArrayList<Post>> posts;
    private MutableLiveData<Post> currentFetchedPost;
    private MutableLiveData<ArrayList<Post>> favoritePosts;
    private MutableLiveData<ArrayList<Post>> myPosts;
    private PostRepositoryInterface repositoryInterface;
    private long lastPostID;


    private PostRepository(@NonNull Retrofit retrofit) {
        repositoryInterface = retrofit.create(PostRepositoryInterface.class);
    }

    public static PostRepository getInstance(@NonNull Retrofit retrofit) {
        if (instance == null) {
            instance = new PostRepository(retrofit);
        }
        return instance;

    }

    public void fetchMorePostsByPageNumber(@NonNull MutableLiveData<ArrayList<Post>> posts, int page) {
        repositoryInterface.fetchPosts(true).enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Post>> call, @NonNull Response<ArrayList<Post>> response) {
                ArrayList<Post> fetchedPosts = response.body();
                assert fetchedPosts != null;
                lastPostID = fetchedPosts.get(fetchedPosts.size() - 1).getPostID();
                if (posts.getValue() != null) {
                    //clear some data if necessary
                    clearData(posts);
                    posts.getValue().addAll(fetchedPosts);
                } else {
                    posts.setValue(fetchedPosts);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Post>> call, Throwable t) {

            }
        });
    }

    private void clearData(@NonNull MutableLiveData<ArrayList<Post>> posts) {
        if (posts.getValue().size() > 20) {

        }
    }

    public MutableLiveData<Post> fetchPostByID(long id) {
        currentFetchedPost = new MutableLiveData<>();
        repositoryInterface.fetchPostByID(id).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                currentFetchedPost.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
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
    public MutableLiveData<ArrayList<Post>> fetchFavoritePosts(String userID) {
        //prepare data to be returned on the main thread
        favoritePosts = new MutableLiveData<>();
        //start fetching the other data on the other thread
        repositoryInterface.fetchFavoritePostsByUserID(userID, true).enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                ArrayList<Post> fetchedPosts = response.body();
                fetchedPosts.forEach(post -> post.setFavorite(true));
                favoritePosts.setValue(fetchedPosts);
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {

            }
        });
        return favoritePosts;
    }

    public MutableLiveData<ArrayList<Post>> fetchMyPosts(String userID) {
        myPosts = new MutableLiveData<>();
        repositoryInterface.fetchMyPosts(userID, true).enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                myPosts.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {

            }
        });
        return myPosts;
    }


    public void addPostToFavorites(long postID, String userID) {
        repositoryInterface.addPostToFavorites(true, postID, userID).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });
    }

    public void insertPost(@NonNull SerializePost serializePost) {
        repositoryInterface.uploadPost(true, serializePost).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                addNewDataAndNotify(posts, response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void addNewDataAndNotify(MutableLiveData<ArrayList<Post>> data, Post post) {
        if (data != null && data.getValue() != null) {
            ArrayList<Post> newData = new ArrayList<Post>(data.getValue());
            newData.add(post);
            data.setValue(newData);
        }
    }


    public void deletePostFromFavorites(long postID, String userID) {
        //  repositoryInterface.deletePostFromFavorites(postID,userID);
    }


    public MutableLiveData<ArrayList<Post>> fetchNewPosts() {
        if (posts == null) {
            posts = new MutableLiveData<>();
        }
        repositoryInterface.fetchNewPosts(true, lastPostID).enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {

            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {

            }
        });

        return posts;
    }
}
