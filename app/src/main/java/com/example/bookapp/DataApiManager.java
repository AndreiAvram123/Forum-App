package com.example.bookapp;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.models.Comment;
import com.example.bookapp.models.Post;
import com.example.bookapp.models.PostBuilder;
import com.example.bookapp.models.PostConverter;

import java.util.ArrayList;
import java.util.HashMap;

class DataApiManager {
    private Activity activity;
    private RequestQueue requestQueue;
    private DataApiManagerCallback dataApiManagerCallback;
    private static final String URL_LATEST_POSTS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?recentPosts";
    private static final String URL_POST_AUTOCOMPLETE = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?suggestionQuery=%s";
    private static final String URL_POST_COMMENTS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?postID=%s&comments";
    private static final String URL_POST_DETAILS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?postID=%s";


    DataApiManager(Activity activity) {
        this.activity = activity;
        requestQueue = Volley.newRequestQueue(activity);
        dataApiManagerCallback = (DataApiManagerCallback) activity;
    }


    void pushRequestLatestPosts() {
        StringRequest randomRecipesRequest = new StringRequest(Request.Method.GET, URL_LATEST_POSTS,
                (response) -> {
                    ArrayList<Post> latestPosts = PostConverter.getSmallDataPostsFromJsonArray(response);
                    dataApiManagerCallback.onLatestPostsDataReady(latestPosts);
                }, Throwable::printStackTrace);

        requestQueue.add(randomRecipesRequest);

    }


    void pushRequestGetPostDetails(int postID) {

        StringRequest postDetailsRequest = new StringRequest(Request.Method.GET, String.format(URL_POST_DETAILS, postID), (response) -> {
            PostBuilder postBuilder = new PostBuilder();
            PostConverter.getFullPostDetailsFromJson(response, postBuilder);
            pushRequestGetPostComments(postBuilder.createPost(), postID);
        }, Throwable::printStackTrace);

        requestQueue.add(postDetailsRequest);
    }

    private void pushRequestGetPostComments(@NonNull Post post, @NonNull int postID) {
        StringRequest postCommentsRequest = new StringRequest(Request.Method.GET, String.format(URL_POST_COMMENTS, postID),
                (response) -> {
                    ArrayList<Comment> comments = PostConverter.getCommentsFromJson(response);
                    dataApiManagerCallback.onPostDetailsReady(post, comments, null);
                }, Throwable::printStackTrace);
        requestQueue.add(postCommentsRequest);
    }


    void pushRequestAutocomplete(String query) {
        String formattedAutocompleteURl = String.format(URL_POST_AUTOCOMPLETE, query);
        //push request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, formattedAutocompleteURl, (response) ->
        {
            ArrayList<Post> suggestions = PostConverter.getAutocompleteSuggestionFromJson(response);
            activity.runOnUiThread(() -> dataApiManagerCallback.onAutocompleteSuggestionsReady(suggestions));
        },
                (error) -> {
                });
        requestQueue.add(stringRequest);
    }


    public interface DataApiManagerCallback {
        void onLatestPostsDataReady(ArrayList<Post> latestPosts);

        void onPostDetailsReady(@NonNull Post post, @Nullable ArrayList<Comment> comments, @Nullable ArrayList<Post> similarPosts);

        void onPostSearchReady(ArrayList<Post> data);

        void onAutocompleteSuggestionsReady(ArrayList<Post> data);

    }
}
