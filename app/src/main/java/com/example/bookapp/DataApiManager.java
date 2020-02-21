package com.example.bookapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.models.Comment;
import com.example.bookapp.models.Post;
import com.example.bookapp.models.PostBuilder;
import com.example.bookapp.models.PostConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private static final String UPLOAD_IMAGE_URL = "";
    private int currentUserID = 2;
    private UploadFileHandler uploadFileHandler;

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


        JsonObjectRequest uploadCommentRequest = new JsonObjectRequest(Request.Method.POST, URL_UPLOAD_COMMENT, postBody, response -> {

        }, error -> {

        });

        requestQueue.add(uploadCommentRequest);
    }

    void uploadPost(Post post) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("postTitle", post.getPostTitle());
            postBody.put("postDate", post.getPostDate());
            postBody.put("postAuthorID", currentUserID);
            postBody.put("postCategory", post.getPostCategory());
            //todo
            //upload image via post
            postBody.put("postImage", post.getPostImage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public interface DataApiManagerCallback {
        void onLatestPostsDataReady(ArrayList<Post> latestPosts);

        void onPostDetailsReady(@NonNull Post post, @Nullable ArrayList<Comment> comments, @Nullable ArrayList<Post> similarPosts);

        void onPostSearchReady(ArrayList<Post> data);

        void onAutocompleteSuggestionsReady(ArrayList<Post> data);

    }

     static class UploadFileHandler extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            startUpload(strings[0]);
            return null;
        }

        private void startUpload(String sourceFileUri) {
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            File sourceFile = new File(sourceFileUri);
            if (sourceFile.isFile()) {

                try {
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    URL url = new URL(UPLOAD_IMAGE_URL);

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE",
                            "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("bill", sourceFileUri);
                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                            + sourceFileUri + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math
                                .min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0,
                                bufferSize);

                    }

                    // send multipart form data necesssary after file
                    // data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens
                            + lineEnd);

                    // Responses from the server (code and message)
                    int serverResponseCode = conn.getResponseCode();

                    if (serverResponseCode == 200) {
                        Log.d("Debug", "Fuck me it worked");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
