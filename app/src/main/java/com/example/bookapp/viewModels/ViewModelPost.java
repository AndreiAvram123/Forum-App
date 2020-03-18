package com.example.bookapp.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.activities.AppUtilities;
import com.example.bookapp.models.Post;
import com.example.dataLayer.dataObjectsToSerialize.SerializePost;
import com.example.dataLayer.repositories.PostRepository;

import java.util.ArrayList;

import retrofit2.converter.gson.GsonConverterFactory;

public class ViewModelPost extends ViewModel {
    private MutableLiveData<ArrayList<Post>> currentFetchedPosts;
    private MutableLiveData<ArrayList<Post>> currentlyDisplayedPosts;

    private final MutableLiveData<ArrayList<Post>> autocompleteResults = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Post>> previousAutocompleteResults = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Post>> myPosts;
    private PostRepository postRepository;


    public ViewModelPost() {
        super();
        postRepository = PostRepository.getInstance(AppUtilities.getRetrofit());
    }


    public MutableLiveData<ArrayList<Post>> getMyPosts(String userID) {
        if (myPosts == null) {
            //should fetch posts
            myPosts = postRepository.fetchMyPosts(userID);
        }
        return myPosts;
    }

    public MutableLiveData<Post> getPost(long id) {
        return postRepository.fetchPostByID(id);
    }





    public LiveData<ArrayList<Post>> getPostsFromPage(int page) {
        if (currentFetchedPosts == null) {
            currentFetchedPosts = new MutableLiveData<>();
        }
        postRepository.fetchMorePostsByPageNumber(currentFetchedPosts, page);
        return currentFetchedPosts;
    }



    public MutableLiveData<ArrayList<Post>> getFavoritePosts(String userID) {
        if (currentlyDisplayedPosts == null) {
            currentlyDisplayedPosts = postRepository.fetchFavoritePosts(userID);
        }
        return currentlyDisplayedPosts;
    }


    public void addPostToFavorites(Post post, String userID) {
        postRepository.addPostToFavorites(post.getPostID(), userID);
        //if the favoritePosts data has been fetched you need to update the ui
        if (currentlyDisplayedPosts != null && currentlyDisplayedPosts.getValue() != null) {
            ArrayList<Post> newData = new ArrayList<>(currentlyDisplayedPosts.getValue());
            post.setFavorite(true);
            newData.add(post);
            currentlyDisplayedPosts.setValue(newData);
        }
    }

    public void deletePostFromFavorites(Post post, String userID) {
        postRepository.deletePostFromFavorites(post.getPostID(), userID);
        if (currentlyDisplayedPosts != null && currentlyDisplayedPosts.getValue() != null) {
            ArrayList<Post> newData = new ArrayList<>(currentlyDisplayedPosts.getValue());
            newData.remove(post);
            //notify the observers
            currentlyDisplayedPosts.setValue(newData);
        }
    }

    public void addPost(@NonNull SerializePost serializePost) {
        postRepository.insertPost(serializePost);
    }

    public void fetchNewPosts() {
        currentFetchedPosts = postRepository.fetchNewPosts();
    }


}
