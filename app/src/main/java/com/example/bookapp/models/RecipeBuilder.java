package com.example.bookapp.models;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeBuilder {
    int id;
    private String name;
    private String imageUrl;
    private String healthPoints;
    private String readyInMinutes;
    private String servings;
    private HashMap<String, Boolean> features;
    private String dishType;
    private ArrayList<String> ingredients;
    private ArrayList<String> instructions;
   private Parcel in;
    public RecipeBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public RecipeBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RecipeBuilder setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public RecipeBuilder setHealthPoints(String healthPoints) {
        this.healthPoints = healthPoints;
        return this;
    }

    public RecipeBuilder setReadyInMinutes(String readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
        return this;
    }

    public RecipeBuilder setServings(String servings) {
        this.servings = servings;
        return this;
    }

    public RecipeBuilder setFeatures(HashMap<String, Boolean> features) {
        this.features = features;
        return this;
    }

    public RecipeBuilder setDishType(String dishType) {
        this.dishType = dishType;
        return this;
    }

    public RecipeBuilder setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public RecipeBuilder setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
        return this;
    }

    RecipeBuilder setIn(Parcel in) {
        this.in = in;
        return this;
    }

    public Recipe createRecipe() {
        if(in!=null){
            return new Recipe(in);
        }

        Recipe recipeCreated =  new Recipe(id, name, imageUrl, readyInMinutes,servings);
        if(recipeCreated.getId() == 0){
            throw new IllegalStateException("ID cannot be 0");
        }
        if(recipeCreated.getName().isEmpty()){
            throw new IllegalStateException("Recipe name cannot be empty");
        }
        if(recipeCreated.getImageUrl() == null || recipeCreated.getImageUrl().isEmpty()){
            throw new IllegalStateException("Image url cannot be empty");
        }
        if(recipeCreated.getReadyInMinutes() == null || recipeCreated.getReadyInMinutes().isEmpty()){
            throw  new IllegalStateException("Ready in minutes cannot be empty");
        }
        if(recipeCreated.getServings() == null || recipeCreated.getServings().isEmpty()){
            throw new IllegalStateException("Servings cannot be empty");
        }
        if(features!=null){
            recipeCreated.setFeatures(features);
        }
        if(dishType!=null){
            recipeCreated.setDishType(dishType);
        }
        if(ingredients!=null){
            recipeCreated.setIngredients(ingredients);
        }
        if(instructions!=null){
            recipeCreated.setInstructions(instructions);
        }


        return recipeCreated;

    }


}