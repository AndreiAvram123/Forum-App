package com.example.bookapp.fragments;

import com.example.bookapp.models.Recipe;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;

public class SavedRecipesObject {
    public HashMap<String, Recipe> savedRecipes = new HashMap<>();

    public HashMap<String, Recipe> getSavedRecipes() {
        return savedRecipes;
    }

    public void setSavedRecipes(HashMap<String, Recipe> savedRecipes) {
        this.savedRecipes = savedRecipes;
    }

    public void addRecipe(Recipe recipe){
        savedRecipes.put(recipe.getId()+"",recipe);
    }
}
