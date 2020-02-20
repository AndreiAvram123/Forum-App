package com.example.bookapp.models;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PostConverter {
    private static final String defaultImageURL = "http://sgb967.poseidon.salford.ac.uk/cms/%s";

    public static ArrayList<Post> getSmallDataPostsFromJsonArray(String json) {
        ArrayList<Post> dataToReturn = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject postJson = jsonArray.getJSONObject(i);
                dataToReturn.add(new PostBuilder()
                        .setPostID(postJson.getInt("postID"))
                        .setPostTitle(postJson.getString("postTitle"))
                        .setPostImage(String.format(defaultImageURL, postJson.getString("postImage")))
                        .createPost());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataToReturn;
    }

    public static HashMap<Integer, String> getAutocompleteSuggestionFromJson(String response) {
        HashMap<Integer, String> dataToReturn = new HashMap<>();
        try {
            JSONArray suggestionsJson = new JSONArray(response);
            for (int i = 0; i < suggestionsJson.length(); i++) {
                JSONObject jsonObject = suggestionsJson.getJSONObject(i);
                dataToReturn.put(jsonObject.getInt("postID"), jsonObject.getString("postTitle"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataToReturn;
    }


    public static void getFullPostDetailsFromJson(@NonNull String response, @NonNull PostBuilder postBuilder) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            postBuilder.setPostID(jsonObject.getInt("postID"))
                    .setPostTitle(jsonObject.getString("postTitle"))
                    .setPostDate(jsonObject.getString("postDate"))
                    .setPostAuthor(jsonObject.getString("postAuthor"))
                    .setPostImage(String.format(defaultImageURL, jsonObject.getString("postImage")))
                    .setPostCategory(jsonObject.getString("postCategory"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Comment> getCommentsFromJson(@NonNull String response) {
        ArrayList<Comment> comments = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                CommentBuilder commentBuilder = new CommentBuilder();
                JSONObject commentJSON = jsonArray.getJSONObject(i);
                commentBuilder.setCommentID(commentJSON.getInt("commentID"))
                        .setCommentAuthor(commentJSON.getString("commentAuthor"))
                        .setCommentContent(commentJSON.getString("commentContent"))
                        .setCommentDate(commentJSON.getString("commentDate"));
                comments.add(commentBuilder.createComment());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comments;
    }
}
