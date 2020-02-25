package com.example.bookapp.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.ApiManager;

import java.util.ArrayList;

public class ViewModelPost extends ViewModel implements ApiManager.ApiManagerDataCallback {
    private final MutableLiveData<ArrayList<Post>> latestPosts = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Post>> autocompleteResults = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Post>> previousAutocompleteResults = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Post>> savedPosts = new MutableLiveData<>();

    public ViewModelPost() {
        super();
        initializeFields();
    }

    private void initializeFields() {
        this.autocompleteResults.setValue(new ArrayList<>());
        this.latestPosts.setValue(new ArrayList<>());
        this.previousAutocompleteResults.setValue(new ArrayList<>());
        this.savedPosts.setValue(new ArrayList<>());
    }

    public void setSavedPosts(ArrayList<Post> savedPosts) {
        this.savedPosts.setValue(savedPosts);
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

    public void setLatestPosts(ArrayList<Post> latestPosts) {
        this.latestPosts.setValue(latestPosts);
    }

    public void setAutocompleteResults(ArrayList<Post> autocompleteResults) {
        this.autocompleteResults.setValue(autocompleteResults);
    }

    public void setPreviousAutocompleteResults(ArrayList<Post> previousAutocompleteResults) {
        this.previousAutocompleteResults.setValue(previousAutocompleteResults);
    }


    @Override
    public void onLatestPostsDataReady(ArrayList<Post> latestPosts) {

    }

    @Override
    public void onPostDetailsReady(@NonNull Post post, @Nullable ArrayList<Comment> comments, @Nullable ArrayList<Post> similarPosts) {

    }

    @Override
    public void onPostSearchReady(ArrayList<Post> data) {

    }

    @Override
    public void onAutocompleteSuggestionsReady(ArrayList<Post> data) {

    }

    @Override
    public void onSavedPostsReady(ArrayList<Post> savedPosts) {

    }
}
