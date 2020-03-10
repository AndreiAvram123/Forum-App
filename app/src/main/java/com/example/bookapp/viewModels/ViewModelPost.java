package com.example.bookapp.viewModels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.activities.AppUtilities;
import com.example.bookapp.api.ApiManager;
import com.example.bookapp.dataLayer.repositories.PostRepository;
import com.example.bookapp.models.Comment;
import com.example.bookapp.models.Post;

import java.util.ArrayList;

public class ViewModelPost extends ViewModel implements ApiManager.ApiManagerDataCallback {
    private MutableLiveData<ArrayList<Post>> currentPosts;
    private MutableLiveData<ArrayList<Post>> favoritePosts;


    private final MutableLiveData<ArrayList<Post>> autocompleteResults = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Post>> previousAutocompleteResults = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Post>> myPosts;
    private PostRepository postRepository;

    public ViewModelPost() {
        super();
        postRepository = PostRepository.getInstance(AppUtilities.getRetrofit());
        initializeFields();
    }

    private void initializeFields() {
        this.autocompleteResults.setValue(new ArrayList<>());
        this.previousAutocompleteResults.setValue(new ArrayList<>());
    }


    public MutableLiveData<ArrayList<Post>> getMyPosts(String userID) {
        if (myPosts == null) {
            //should fetch posts
            return postRepository.fetchMyPosts(userID);
        }
        return myPosts;
    }

    public MutableLiveData<Post> getPost(int id) {
        return postRepository.fetchPost(id);
    }

    public MutableLiveData<ArrayList<Comment>> getPostComments(int postID) {
        return postRepository.fetchPostComments(postID);
    }


    public void removeFavoritePost(Post postToRemove) {
        ArrayList<Post> newSavedPosts = new ArrayList<>(favoritePosts.getValue());
        newSavedPosts.remove(postToRemove);
        favoritePosts.setValue(newSavedPosts);
    }


    public LiveData<ArrayList<Post>> getCurrentPosts() {
        //no need to fetch the data again if the current posts is not null
        if (currentPosts == null) {
            //fetch the current posts if the current posts are not yet set
            currentPosts = postRepository.fetchCurrentPosts();

        }
        return currentPosts;
    }

    public LiveData<ArrayList<Post>> getAutocompleteResults() {

        return autocompleteResults;
    }

    public LiveData<ArrayList<Post>> getPreviousAutocompleteResults() {

        return previousAutocompleteResults;
    }

    public MutableLiveData<ArrayList<Post>> getFavoritePosts(String userID) {
        if (favoritePosts == null) {
            favoritePosts = postRepository.fetchFavoritePosts(userID);
        }
        return favoritePosts;
    }


    public void addPostToFavorites(Post post, String userID) {
        postRepository.addPostToFavorites(post.getPostID(), userID);
        //if the favoritePosts data has been fetched you need to update the ui
        if (favoritePosts != null && favoritePosts.getValue() != null) {
            ArrayList<Post> newData = new ArrayList<>(favoritePosts.getValue());
            post.setFavorite(true);
            newData.add(post);
            favoritePosts.setValue(newData);
        }
    }

    public void deletePostFromFavorites(Post post, String userID) {
        postRepository.deletePostFromFavorites(post.getPostID(), userID);
        if (favoritePosts != null && favoritePosts.getValue() != null) {
            ArrayList<Post> newData = new ArrayList<>(favoritePosts.getValue());
            newData.remove(post);
            //notify the observers
            favoritePosts.setValue(newData);
        }
    }




    @Override
    public void onLatestPostsDataReady(ArrayList<Post> latestPosts) {
        this.currentPosts.setValue(latestPosts);
    }

    @Override
    public void onPostDetailsReady(@NonNull Post post, @Nullable ArrayList<Comment> comments, @Nullable ArrayList<Post> similarPosts) {

    }

    @Override
    public void onPostSearchReady(ArrayList<Post> data) {

    }

    @Override
    public void onAutocompleteSuggestionsReady(ArrayList<Post> data) {
        this.autocompleteResults.setValue(data);
    }

    @Override
    public void onSavedPostsReady(ArrayList<Post> savedPosts) {
        this.favoritePosts.setValue(savedPosts);
    }

    @Override
    public void onMyPostsReady(@NonNull ArrayList<Post> myPosts) {
     this.myPosts.setValue(myPosts);
    }

    @Override
    public void onNewPostsReady(@NonNull ArrayList<Post> fetchedPosts) {
        if (currentPosts.getValue() != null) {
            ArrayList<Post> newData = new ArrayList<>(currentPosts.getValue());
            newData.addAll(fetchedPosts);
            currentPosts.setValue(newData);
        } else {
            currentPosts.setValue(fetchedPosts);
        }
    }


}
