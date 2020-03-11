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

public class ViewModelPost extends ViewModel {
    private MutableLiveData<ArrayList<Post>> currentDisplayedPosts;
    private MutableLiveData<ArrayList<Post>> favoritePosts;

    private final MutableLiveData<ArrayList<Post>> autocompleteResults = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Post>> previousAutocompleteResults = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Post>> myPosts;
    private PostRepository postRepository;


    public ViewModelPost() {
        super();
        postRepository = PostRepository.getInstance(AppUtilities.getRetrofit(null));
    }


    public MutableLiveData<ArrayList<Post>> getMyPosts(String userID) {
        if (myPosts == null) {
            //should fetch posts
            myPosts = postRepository.fetchMyPosts(userID);
        }
        return myPosts;
    }

    public MutableLiveData<Post> getPost(int id) {
        return postRepository.fetchPost(id);
    }




    public void removeFavoritePost(Post postToRemove) {
        ArrayList<Post> newSavedPosts = new ArrayList<>(favoritePosts.getValue());
        newSavedPosts.remove(postToRemove);
        favoritePosts.setValue(newSavedPosts);
    }


    public LiveData<ArrayList<Post>> getCurrentDisplayedPosts() {
        //no need to fetch the data again if the current posts is not null
        if (currentDisplayedPosts == null) {
            //fetch the current posts if the current posts are not yet set
            currentDisplayedPosts = postRepository.fetchCurrentPosts();

        }
        return currentDisplayedPosts;
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

    public void addPost(@NonNull SerializePost serializePost) {
        postRepository.insertPost(serializePost);
    }

    public void fetchNewPosts() {
        currentDisplayedPosts = postRepository.fetchNewPosts();
    }

//
//    @Override
//    public void onLatestPostsDataReady(ArrayList<Post> latestPosts) {
//        this.currentDisplayedPosts.setValue(latestPosts);
//    }
//
//    @Override
//    public void onPostDetailsReady(@NonNull Post post, @Nullable ArrayList<Comment> comments, @Nullable ArrayList<Post> similarPosts) {
//
//    }
//
//    @Override
//    public void onPostSearchReady(ArrayList<Post> data) {
//
//    }
//
//    @Override
//    public void onAutocompleteSuggestionsReady(ArrayList<Post> data) {
//        this.autocompleteResults.setValue(data);
//    }
//
//    @Override
//    public void onSavedPostsReady(ArrayList<Post> savedPosts) {
//        this.favoritePosts.setValue(savedPosts);
//    }
//
//    @Override
//    public void onMyPostsReady(@NonNull ArrayList<Post> myPosts) {
//     this.myPosts.setValue(myPosts);
//    }
//
//    @Override
//    public void onNewPostsReady(@NonNull ArrayList<Post> fetchedPosts) {
//        if (currentDisplayedPosts.getValue() != null) {
//            ArrayList<Post> newData = new ArrayList<>(currentDisplayedPosts.getValue());
//            newData.addAll(fetchedPosts);
//            currentDisplayedPosts.setValue(newData);
//        } else {
//            currentDisplayedPosts.setValue(fetchedPosts);
//        }
//    }



}
