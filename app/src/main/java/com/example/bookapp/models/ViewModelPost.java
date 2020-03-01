package com.example.bookapp.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.activities.ApiManager;

import java.util.ArrayList;

public class ViewModelPost extends ViewModel implements ApiManager.ApiManagerDataCallback {
    private final MutableLiveData<ArrayList<Post>> latestPosts = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Post>> autocompleteResults = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Post>> previousAutocompleteResults = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Post>> savedPosts = new MutableLiveData<>();
    private final MutableLiveData<Post> currentPost = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Comment>> currentPostComments = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Post>> myPosts = new MutableLiveData<>();
    public ViewModelPost() {
        super();
        initializeFields();
    }

    private void initializeFields() {
        this.autocompleteResults.setValue(new ArrayList<>());
        this.previousAutocompleteResults.setValue(new ArrayList<>());
        this.savedPosts.setValue(new ArrayList<>());
        this.myPosts.setValue(new ArrayList<>());
    }

    public MutableLiveData<ArrayList<Post>> getMyPosts() {
        return myPosts;
    }

    public MutableLiveData<Post> getCurrentPost() {
        return currentPost;
    }

    public MutableLiveData<ArrayList<Comment>> getCurrentPostComments() {
        return currentPostComments;
    }

    public void addFavoritePost(Post savedPost) {
        ArrayList<Post> newSavedPosts = new ArrayList<>(savedPosts.getValue());
        newSavedPosts.add(savedPost);
        savedPosts.setValue(newSavedPosts);

    }

    public void removeFavoritePost(Post postToRemove) {
        ArrayList<Post> newSavedPosts = new ArrayList<>(savedPosts.getValue());
        newSavedPosts.remove(postToRemove);
        savedPosts.setValue(newSavedPosts);
    }


    public LiveData<ArrayList<Post>> getLatestPosts() {
        return latestPosts;
    }

    public LiveData<ArrayList<Post>> getAutocompleteResults() {

        return autocompleteResults;
    }

    public LiveData<ArrayList<Post>> getPreviousAutocompleteResults() {

        return previousAutocompleteResults;
    }

    public MutableLiveData<ArrayList<Post>> getSavedPosts() {
        return savedPosts;
    }


    @Override
    public void onLatestPostsDataReady(ArrayList<Post> latestPosts) {
        this.latestPosts.setValue(latestPosts);
    }

    @Override
    public void onPostDetailsReady(@NonNull Post post, @Nullable ArrayList<Comment> comments, @Nullable ArrayList<Post> similarPosts) {
        this.currentPost.setValue(post);
        this.currentPostComments.setValue(comments);
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
        this.savedPosts.setValue(savedPosts);
    }

    @Override
    public void onMyPostsReady(@NonNull ArrayList<Post> myPosts) {
     this.myPosts.setValue(myPosts);
    }
}
