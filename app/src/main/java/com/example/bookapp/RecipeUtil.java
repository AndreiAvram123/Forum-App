package com.example.bookapp;

import androidx.annotation.NonNull;

import com.example.bookapp.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RecipeUtil {
    public static String getDishType(JSONObject recipeJson) throws JSONException {
        JSONArray dishTypes  = recipeJson.getJSONArray("dishTypes");
        return "#" +dishTypes.get(0).toString();
    }

    public static HashMap<String, Boolean> getRecipeFeatures(JSONObject recipeJson) throws JSONException {
        HashMap<String,Boolean> features = new HashMap<>();


        features.put("#Vegetarian",recipeJson.getBoolean("vegetarian"));
        features.put("#Vegan",recipeJson.getBoolean("vegan"));
        features.put("#Gluten Free",recipeJson.getBoolean("glutenFree"));
        return features;
    }

    public static  ArrayList<Recipe> getRecipesListFromJson(String json) {
        ArrayList<Recipe> results = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            //get the recipes array
            JSONArray recipes = jsonObject.getJSONArray("recipes");
            for (int i = 0; i < recipes.length(); i++) {
                JSONObject recipeJson = recipes.getJSONObject(i);



                Recipe recipeObject = new Recipe(recipeJson.getInt("id"),recipeJson.getString("title"),
                        recipeJson.getString("image"),
                        Integer.toString(recipeJson.getInt("healthScore")),
                        Integer.toString(recipeJson.getInt("readyInMinutes")),
                        Integer.toString(recipeJson.getInt("servings")),
                        RecipeUtil.getRecipeFeatures(recipeJson),
                        RecipeUtil.getDishType(recipeJson),
                        getIngredientsFromJsonArray(recipeJson.getJSONArray("extendedIngredients")),
                        getInstructionsFromJsonArray((recipeJson.getJSONArray("analyzedInstructions")))
                );

                results.add(recipeObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    //todo
    //don't pass null

    public static  ArrayList<Recipe> getSearchResultsFromJson(@NonNull String json) {
        ArrayList<Recipe> results = new ArrayList<>();
        String apiImagePath = "https://spoonacular.com/recipeImages/%s";
        try {
            JSONObject jsonObject = new JSONObject(json);
            //get the recipes array
            JSONArray recipes = jsonObject.getJSONArray("results");
            for (int i = 0; i < recipes.length(); i++) {
                JSONObject recipeJson = recipes.getJSONObject(i);
                Recipe recipeObject = new Recipe(
                        recipeJson.getInt("id"),recipeJson.getString("title"),
                        String.format(apiImagePath, recipeJson.getString("image")),
                        Integer.toString(recipeJson.getInt("healthScore")),
                        Integer.toString(recipeJson.getInt("readyInMinutes")),
                        Integer.toString(recipeJson.getInt("servings")),
                        RecipeUtil.getRecipeFeatures(recipeJson),
                        RecipeUtil.getDishType(recipeJson),
                       null,
                        getInstructionsFromJsonArray((recipeJson.getJSONArray("analyzedInstructions")))
                );
                results.add(recipeObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static ArrayList<String>getInstructionsFromJsonArray(@NonNull JSONArray instructionsJson) throws JSONException {
         JSONObject firstInstructionsObject = instructionsJson.getJSONObject(0);
        JSONArray steps = firstInstructionsObject.getJSONArray("steps");
        ArrayList<String> instructionsStringList = new ArrayList<>();
        for(int i=0;i<steps.length();i++){
            JSONObject step = steps.getJSONObject(i);
            instructionsStringList.add(step.getString("step"));
        }
        Collections.reverse(instructionsStringList);
        return instructionsStringList;
    }

    public static ArrayList<String>getIngredientsFromJsonArray(@NonNull JSONArray ingredients) throws JSONException {
        ArrayList<String> formattedIngredients = new ArrayList<>();
        for(int i=0;i<ingredients.length();i++){
            JSONObject ingredientJson = ingredients.getJSONObject(i);
            String ingredientFormatted = "%.2f %s of %s";
            formattedIngredients.add(String.format(ingredientFormatted,
                    ingredientJson.getDouble("amount"),ingredientJson.getString("unit"),
                    ingredientJson.getString("name")));

        }
        return formattedIngredients;
    }
}
