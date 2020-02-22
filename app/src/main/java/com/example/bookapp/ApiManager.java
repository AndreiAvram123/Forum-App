package com.example.bookapp;

import android.content.Context;
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
import com.example.bookapp.models.ApiConstants;
import com.example.bookapp.models.NonUploadedPost;
import com.example.bookapp.models.Post;
import com.example.bookapp.models.PostConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.bookapp.models.ApiConstants.UPLOAD_IMAGE_URL;
import static com.example.bookapp.models.ApiConstants.URL_LATEST_POSTS;
import static com.example.bookapp.models.ApiConstants.URL_POST_AUTOCOMPLETE;
import static com.example.bookapp.models.ApiConstants.URL_POST_COMMENTS;
import static com.example.bookapp.models.ApiConstants.URL_POST_DETAILS;
import static com.example.bookapp.models.ApiConstants.URL_UPLOAD_COMMENT;

public class ApiManager {
    private RequestQueue requestQueue;
    private ApiManagerDataCallback apiManagerDataCallback;
    private ApiManagerAuthenticationCallback apiManagerAuthenticationCallback;
    private int currentUserID = 2;
    private static ApiManager instance;

    public static synchronized ApiManager getInstance(@NonNull Context context) {

        if (instance == null) {
            instance = new ApiManager(context);
        }
        return instance;
    }

    private ApiManager(@NonNull Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void setApiManagerDataCallback(@NonNull ApiManagerDataCallback callback) {
        this.apiManagerDataCallback = callback;
    }

    public void setApiManagerAuthenticationCallback(@NonNull ApiManagerAuthenticationCallback callback) {
        this.apiManagerAuthenticationCallback = callback;
    }


    public void authenticateWithAccountID(String id) {
        String formattedURL = String.format(ApiConstants.URL_AUTHENTICATE_ACCOUNT_ID, id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                formattedURL, (response) -> {
            try {
                JSONObject responseJson = new JSONObject(response);
                apiManagerAuthenticationCallback.onAuthenticationResponse(responseJson.getInt("responseCode"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
                , Throwable::printStackTrace);
        requestQueue.add(stringRequest);
    }

    void pushRequestLatestPosts() {
        StringRequest randomRecipesRequest = new StringRequest(Request.Method.GET, URL_LATEST_POSTS,
                (response) -> {
                    ArrayList<Post> latestPosts = PostConverter.getSmallDataPostsFromJsonArray(response);
                    apiManagerDataCallback.onLatestPostsDataReady(latestPosts);
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
                    apiManagerDataCallback.onPostDetailsReady(post, comments, null);
                }, Throwable::printStackTrace);
        requestQueue.add(postCommentsRequest);
    }


    void pushRequestAutocomplete(String query) {
        String formattedAutocompleteURl = String.format(URL_POST_AUTOCOMPLETE, query);
        //push request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, formattedAutocompleteURl, (response) ->
        {
            ArrayList<Post> suggestions = PostConverter.getAutocompleteSuggestionFromJson(response);
            apiManagerDataCallback.onAutocompleteSuggestionsReady(suggestions);
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


        JsonObjectRequest uploadCommentRequest = new JsonObjectRequest(Request.Method.POST, URL_UPLOAD_COMMENT, postBody, response -> Log.d("Debug", response.toString()), error -> {

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
            postBody.put("postContent", post.getPostContent());
            postBody.put("filename", post.getImageName());
            postBody.put("image", encodedString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest uploadCommentRequest = new JsonObjectRequest(Request.Method.POST, UPLOAD_IMAGE_URL, postBody, response -> {
            Log.d("Debug", response.toString());
        }, error -> {

        });
        requestQueue.add(uploadCommentRequest);
    }

    void pushRequestGetSavedPosts() {

        String formattedURLSavedPosts = String.format(ApiConstants.URL_SAVED_POSTS, currentUserID);
        //push request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, formattedURLSavedPosts, (response) ->
        {
            ArrayList<Post> savedPosts = PostConverter.getSmallDataPostsFromJsonArray(response);
            apiManagerDataCallback.onSavedPostsReady(savedPosts);
        },
                (error) -> {
                });
        requestQueue.add(stringRequest);
    }


    public interface ApiManagerDataCallback {
        void onLatestPostsDataReady(ArrayList<Post> latestPosts);

        void onPostDetailsReady(@NonNull Post post, @Nullable ArrayList<Comment> comments, @Nullable ArrayList<Post> similarPosts);

        void onPostSearchReady(ArrayList<Post> data);

        void onAutocompleteSuggestionsReady(ArrayList<Post> data);

        void onSavedPostsReady(ArrayList<Post> savedPosts);

    }

    public interface ApiManagerAuthenticationCallback {
        void onAuthenticationResponse(int responseCode);
    }
}
