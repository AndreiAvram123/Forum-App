package com.example.bookapp;

import androidx.annotation.NonNull;

import com.example.bookapp.models.Recipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RecipeUtil {
    private static String apiImagePath = "https://spoonacular.com/recipeImages/%s";

    public static String getDishType(JSONObject recipeJson) throws JSONException {
        JSONArray dishTypes = recipeJson.getJSONArray("dishTypes");
        return "#" + dishTypes.get(0).toString();
    }

    public static HashMap<String, Boolean> getRecipeFeatures(JSONObject recipeJson) throws JSONException {
        HashMap<String, Boolean> features = new HashMap<>();


        features.put("#Vegetarian", recipeJson.getBoolean("vegetarian"));
        features.put("#Vegan", recipeJson.getBoolean("vegan"));
        features.put("#Gluten Free", recipeJson.getBoolean("glutenFree"));
        return features;
    }

    public static ArrayList<Recipe> getRecipesListFromJson(String json) {
        ArrayList<Recipe> results = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            //get the recipes array
            JSONArray recipes = jsonObject.getJSONArray("recipes");
            for (int i = 0; i < recipes.length(); i++) {
                JSONObject recipeJson = recipes.getJSONObject(i);


                Recipe recipeObject = new Recipe(recipeJson.getInt("id"), recipeJson.getString("title"),
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


    static ArrayList<Recipe> getSearchResultsFromJson(@NonNull String json) {
        ArrayList<Recipe> results = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            //get the recipes array
            JSONArray recipes = jsonObject.getJSONArray("results");
            for (int i = 0; i < recipes.length(); i++) {
                JSONObject recipeJson = recipes.getJSONObject(i);
                Recipe recipeObject = new Recipe(
                        recipeJson.getInt("id"), recipeJson.getString("title"),
                        String.format(apiImagePath, recipeJson.getString("image"))
                );
                results.add(recipeObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    static ArrayList<Recipe> getSimilarRecipesFromJson(@NonNull String json) {
        ArrayList<Recipe> similarRecipes = new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = null;

                jsonObject = jsonArray.getJSONObject(i);
                Recipe recipe = new Recipe(
                        jsonObject.getInt("id"), jsonObject.getString("title"),
                        String.format(apiImagePath, jsonObject.getString("image")));
                similarRecipes.add(recipe);


            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        return similarRecipes;
    }

    static ArrayList<String> getInstructionsFromJsonArray(@NonNull JSONArray instructionsJson) throws JSONException {
        ArrayList<String> instructionsStringList = new ArrayList<>();
        if (instructionsJson.length() != 0) {
            JSONObject firstInstructionsObject = instructionsJson.getJSONObject(0);
            JSONArray steps = firstInstructionsObject.getJSONArray("steps");
            for (int i = 0; i < steps.length(); i++) {
                JSONObject step = steps.getJSONObject(i);
                instructionsStringList.add(step.getString("step"));
            }
            Collections.reverse(instructionsStringList);
        }
        return instructionsStringList;
    }

    static ArrayList<String> getIngredientsFromJsonArray(@NonNull JSONArray ingredients) throws JSONException {
        ArrayList<String> formattedIngredients = new ArrayList<>();
        for (int i = 0; i < ingredients.length(); i++) {
            JSONObject ingredientJson = ingredients.getJSONObject(i);
            String ingredientFormatted = "%.2f %s of %s";
            formattedIngredients.add(String.format(ingredientFormatted,
                    ingredientJson.getDouble("amount"), ingredientJson.getString("unit"),
                    ingredientJson.getString("name")));

        }
        return formattedIngredients;
    }

    static Recipe getRecipeFromJson(String jsonString) {
        try {
            JSONObject recipeJson = new JSONObject(jsonString);
            return new Recipe(recipeJson.getInt("id"), recipeJson.getString("title"),
                    recipeJson.getString("image"),
                    Integer.toString(recipeJson.getInt("healthScore")),
                    Integer.toString(recipeJson.getInt("readyInMinutes")),
                    Integer.toString(recipeJson.getInt("servings")),
                    RecipeUtil.getRecipeFeatures(recipeJson),
                    RecipeUtil.getDishType(recipeJson),
                    getIngredientsFromJsonArray(recipeJson.getJSONArray("extendedIngredients")),
                    getInstructionsFromJsonArray((recipeJson.getJSONArray("analyzedInstructions")))
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;


    }

    static HashMap<Integer, String> getAutocompleteSuggestionFromJson(String fetchedData) {
        HashMap<Integer, String> fetchedSuggestions = new HashMap<>();
        try {
            JSONArray result = new JSONArray(fetchedData);
            for (int index = 0; index < result.length(); index++) {
                JSONObject suggestion = result.getJSONObject(index);
                fetchedSuggestions.put(suggestion.getInt("id"), suggestion.getString("title"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fetchedSuggestions;
    }

}
