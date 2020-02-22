package com.example.bookapp;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.models.Comment;
import com.example.bookapp.models.NonUploadedPost;
import com.example.bookapp.models.Post;
import com.example.bookapp.models.PostConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class DataApiManager {
    private Activity activity;
    private RequestQueue requestQueue;
    private DataApiManagerCallback dataApiManagerCallback;
    private static final String URL_LATEST_POSTS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?recentPosts";
    private static final String URL_POST_AUTOCOMPLETE = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?suggestionQuery=%s";
    private static final String URL_POST_COMMENTS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?postID=%s&comments";
    private static final String URL_POST_DETAILS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?postID=%s";
    private static final String URL_UPLOAD_COMMENT = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?uploadComment";
    private static final String UPLOAD_IMAGE_URL = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?uploadPost";
    private int currentUserID = 2;

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
            Post.PostBuilder postBuilder = new Post.PostBuilder();
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

    void uploadNewComment(Comment comment) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("commentUserID", currentUserID);
            postBody.put("commentDate", comment.getCommentDate());
            postBody.put("commentText", comment.getCommentContent());
            postBody.put("commentPostID", comment.getPostID());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest uploadCommentRequest = new JsonObjectRequest(Request.Method.POST, URL_UPLOAD_COMMENT, postBody, response -> Log.d("Debug",response.toString()), error -> {

        });

        requestQueue.add(uploadCommentRequest);
    }

    void uploadPost(NonUploadedPost post) {

        String encodedString = Base64.encodeToString(post.getImageBytes(), 0);

        JSONObject postBody = new JSONObject();
        try {
            postBody.put("postTitle", post.getPostTitle());
            postBody.put("postAuthorID", currentUserID);
            postBody.put("postCategory", post.getPostCategory());
            postBody.put("postContent",post.getPostContent());
            postBody.put("filename", post.getImageName());
            postBody.put("image", encodedString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest uploadCommentRequest = new JsonObjectRequest(Request.Method.POST, UPLOAD_IMAGE_URL, postBody, response -> {
            Log.d("Debug",response.toString());
        }, error -> {

        });
        requestQueue.add(uploadCommentRequest);
    }


    public interface DataApiManagerCallback {
        void onLatestPostsDataReady(ArrayList<Post> latestPosts);

        void onPostDetailsReady(@NonNull Post post, @Nullable ArrayList<Comment> comments, @Nullable ArrayList<Post> similarPosts);

        void onPostSearchReady(ArrayList<Post> data);

        void onAutocompleteSuggestionsReady(ArrayList<Post> data);

    }
}
