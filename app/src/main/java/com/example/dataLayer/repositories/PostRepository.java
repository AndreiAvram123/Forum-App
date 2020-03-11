package com.example.dataLayer.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.bookapp.models.Post;
import com.example.bookapp.models.PostConverter;
import com.example.dataLayer.dataObjectsToSerialize.SerializePost;
import com.example.dataLayer.interfaces.PostRepositoryInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostRepository {

    private static PostRepository instance;
    private MutableLiveData<ArrayList<Post>> currentPosts;
    private MutableLiveData<Post> currentFetchedPost;
    private MutableLiveData<ArrayList<Post>> favoritePosts;
    private MutableLiveData<ArrayList<Post>> myPosts;
    private PostRepositoryInterface repositoryInterface;
    private int lastPostID;


    private PostRepository(@NonNull Retrofit retrofit) {
        repositoryInterface = retrofit.create(PostRepositoryInterface.class);
    }

    public static PostRepository getInstance(@NonNull Retrofit retrofit) {
        if (instance == null) {
            instance = new PostRepository(retrofit);
        }
        return instance;

    }

    public MutableLiveData<ArrayList<Post>> fetchCurrentPosts() {
        currentPosts = new MutableLiveData<>();
        repositoryInterface.fetchCurrentPosts(true).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                ArrayList<Post> fetchedPosts = PostConverter.getPostsFromJsonArray(response.body());
                lastPostID = fetchedPosts.get(fetchedPosts.size() - 1).getPostID();
                currentPosts.setValue(fetchedPosts);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        return currentPosts;
    }

    public MutableLiveData<Post> fetchPost(int id) {
        currentFetchedPost = new MutableLiveData<>();
        repositoryInterface.fetchPostByID(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Post.PostBuilder builder = new Post.PostBuilder();
                if (response.body() != null) {
                    PostConverter.getFullPostDetailsFromJson(response.body(), builder);
                }
                currentFetchedPost.setValue(builder.createPost());
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                t.printStackTrace();
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
        repositoryInterface.fetchFavoritePostsByUserID(userID, true).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                ArrayList<Post> fetchedPosts = PostConverter.getPostsFromJsonArray(response.body());
                fetchedPosts.forEach(post -> post.setFavorite(true));
                favoritePosts.setValue(fetchedPosts);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        return favoritePosts;
    }

    public MutableLiveData<ArrayList<Post>> fetchMyPosts(String userID) {
        myPosts = new MutableLiveData<>();
        repositoryInterface.fetchMyPosts(userID, true).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                ArrayList<Post> fetchedPosts = PostConverter.getPostsFromJsonArray(response.body());
                myPosts.setValue(fetchedPosts);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        return myPosts;
    }


    public void addPostToFavorites(int postID, String userID) {
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
                addNewDataAndNotify(currentPosts, response.body());
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


    public void deletePostFromFavorites(int postID, String userID) {
        //  repositoryInterface.deletePostFromFavorites(postID,userID);
    }


    public MutableLiveData<ArrayList<Post>> fetchNewPosts() {
        if (currentPosts == null) {
            currentPosts = new MutableLiveData<>();
        }
         // repositoryInterface.fetchNewPosts(true,lastPostID).enqueue(new Call);

        return currentPosts;
    }
}
