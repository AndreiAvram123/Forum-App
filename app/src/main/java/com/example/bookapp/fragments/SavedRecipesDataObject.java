package com.example.bookapp.fragments;

import com.example.bookapp.models.Post;

import java.util.ArrayList;
import java.util.HashMap;

public class SavedRecipesDataObject {
    public HashMap<String, Post> savedRecipes = new HashMap<>();

    public HashMap<String, Post> getSavedRecipes() {
        return savedRecipes;
    }

    public SavedRecipesDataObject() {

    }

    public SavedRecipesDataObject(ArrayList<Post> posts) {
        posts.forEach(post -> savedRecipes.put(Integer.toString(post.getPostID()), post));
    }

    public void setSavedRecipes(HashMap<String, Post> savedRecipes) {
        this.savedRecipes = savedRecipes;
    }

}
