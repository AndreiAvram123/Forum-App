package com.example.bookapp.fragments;

import com.example.bookapp.models.Post;

import java.util.ArrayList;
import java.util.HashMap;

public class SavedPostsDataObject {

    public HashMap<String, Post> savedPosts = new HashMap<>();

    public HashMap<String, Post> getSavedPosts() {
        return savedPosts;
    }

    public SavedPostsDataObject() {

    }

    public SavedPostsDataObject(ArrayList<Post> posts) {
        posts.forEach(post -> savedPosts.put(Integer.toString(post.getPostID()), post));
    }

    public void setSavedPosts(HashMap<String, Post> savedPosts) {
        this.savedPosts = savedPosts;
    }

}
