package com.example.bookapp.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PostConverter {


    public static ArrayList<Post> getPostsFromJsonArray(String json) {
        String defaultImageURL = "http://sgb967.poseidon.salford.ac.uk/cms/";
        ArrayList<Post> dataToReturn = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject postJson = jsonArray.getJSONObject(i);
                dataToReturn.add(new PostBuilder()
                        .setPostID(postJson.getInt("postID"))
                        .setPostTitle(postJson.getString("postTitle"))
                        .setPostDate(postJson.getString("postDate"))
                        .setPostAuthor(postJson.getString("postAuthor"))
                        .setPostCategory(postJson.getString("postCategory"))
                        .setPostImage(defaultImageURL + postJson.getString("postImage"))
                        .createPost());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Data", dataToReturn.toString());
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
}
