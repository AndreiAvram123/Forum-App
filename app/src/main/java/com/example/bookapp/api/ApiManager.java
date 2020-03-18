package com.example.bookapp.api;

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
import com.example.dataLayer.repositories.FriendsRepository;
import com.example.bookapp.models.ApiConstants;
import com.example.bookapp.models.Comment;
import com.example.bookapp.models.NonUploadedPost;
import com.example.bookapp.models.Post;
import com.example.bookapp.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.bookapp.models.ApiConstants.UPLOAD_IMAGE_URL;
import static com.example.bookapp.models.ApiConstants.URL_POST_AUTOCOMPLETE;
import static com.example.bookapp.models.ApiConstants.URL_UPLOAD_COMMENT;

public class ApiManager {
    private RequestQueue requestQueue;
    private ApiManagerDataCallback apiManagerDataCallback;
    private ApiManagerAuthenticationCallback apiManagerAuthenticationCallback;
    private static ApiManager instance;
    private static final String TAG = ApiManager.class.getSimpleName();
    private FriendsRepository friendsRepository;
    private int lastPostID;

    public static synchronized ApiManager getInstance(@NonNull Context context) {

        if (instance == null) {
            instance = new ApiManager(context);
        }

        return instance;
    }

    private ApiManager(@NonNull Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void setPostDataCallback(@NonNull ApiManagerDataCallback callback) {
        this.apiManagerDataCallback = callback;
    }

    public void setApiManagerAuthenticationCallback(@NonNull ApiManagerAuthenticationCallback callback) {
        this.apiManagerAuthenticationCallback = callback;
    }


    public void authenticateWithThirdPartyAccount(String email) {
        String formattedURL = String.format(ApiConstants.URL_AUTHENTICATE_ACCOUNT_ID, email);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                formattedURL, (response) -> apiManagerAuthenticationCallback.onAuthenticationResponse(response)
                , Throwable::printStackTrace);
        requestQueue.add(stringRequest);
    }




    public void uploadNewComment(Comment comment, String userID) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("commentUserID", userID);
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

    public void uploadPost(NonUploadedPost post, String userID) {

        String encodedString = Base64.encodeToString(post.getImageBytes(), 0);

        JSONObject postBody = new JSONObject();
        try {
            postBody.put("postTitle", post.getPostTitle());
            postBody.put("postAuthorID", userID);
            postBody.put("postCategory", post.getPostCategory());
            postBody.put("postContent", post.getPostContent());
            postBody.put("filename", post.getImageName());
            postBody.put("image", encodedString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest uploadCommentRequest = new JsonObjectRequest(Request.Method.POST, UPLOAD_IMAGE_URL, postBody, response -> {
        }, error -> {

        });
        requestQueue.add(uploadCommentRequest);
    }

    public void createThirdPartyUserAccount(User user) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("accountID", user.getUserID());
            postBody.put("email", user.getEmail());
            postBody.put("username", user.getUsername());
            postBody.put("profilePicture", user.getProfilePictureURL());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest createAccount = new JsonObjectRequest(Request.Method.POST, ApiConstants.URL_CREATE_THIRD_PARTY_ACCOUNT, postBody, response -> {
            apiManagerAuthenticationCallback.onThirdPartyAccountCreated(response);
        }, error -> {

        });
        requestQueue.add(createAccount);
    }





    public interface ApiManagerDataCallback {
        void onLatestPostsDataReady(ArrayList<Post> latestPosts);

        void onPostDetailsReady(@NonNull Post post, @Nullable ArrayList<Comment> comments, @Nullable ArrayList<Post> similarPosts);

        void onPostSearchReady(@NonNull ArrayList<Post> data);

        void onAutocompleteSuggestionsReady(@NonNull ArrayList<Post> data);

        void onSavedPostsReady(@NonNull ArrayList<Post> savedPosts);

        void onMyPostsReady(@NonNull ArrayList<Post> myPosts);


        void onNewPostsReady(@NonNull ArrayList<Post> fetchedPosts);
    }

    public interface ApiManagerAuthenticationCallback {
        void onAuthenticationResponse(String response);

        void onThirdPartyAccountCreated(JSONObject responseCode);
    }



}
