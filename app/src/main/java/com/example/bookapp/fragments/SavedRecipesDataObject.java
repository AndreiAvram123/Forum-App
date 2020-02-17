package com.example.bookapp.fragments;

import com.example.bookapp.models.Recipe;

import java.util.ArrayList;
import java.util.HashMap;

public class SavedRecipesDataObject {
    public HashMap<String, Recipe> savedRecipes = new HashMap<>();

    public HashMap<String, Recipe> getSavedRecipes() {
        return savedRecipes;
    }

    public SavedRecipesDataObject() {

    }

    public SavedRecipesDataObject(ArrayList<Recipe> recipes) {
        recipes.forEach(recipe -> savedRecipes.put(Integer.toString(recipe.getId()), recipe));
    }

    public void setSavedRecipes(HashMap<String, Recipe> savedRecipes) {
        this.savedRecipes = savedRecipes;
    }

}
