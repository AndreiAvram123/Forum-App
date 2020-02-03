package com.example.bookapp;

import androidx.annotation.NonNull;

import com.example.bookapp.models.Recipe;
import com.example.bookapp.models.RecipeBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class RecipeUtil {
    private static String apiImagePath = "https://spoonacular.com/recipeImages/%s";

    private static String getDishType(JSONObject recipeJson) throws JSONException {
        JSONArray dishTypes = recipeJson.getJSONArray("dishTypes");
        if (dishTypes.length() > 0) {
            return "#" + dishTypes.get(0).toString();
        } else {
            return "unknown";
        }
    }

    private static HashMap<String, Boolean> getRecipeFeatures(JSONObject recipeJson) throws JSONException {
        HashMap<String, Boolean> features = new HashMap<>();


        features.put("#Vegetarian", recipeJson.getBoolean("vegetarian"));
        features.put("#Vegan", recipeJson.getBoolean("vegan"));
        features.put("#Gluten Free", recipeJson.getBoolean("glutenFree"));
        return features;
    }

    static ArrayList<Recipe> getRecipesListFromJson(String json) {
        ArrayList<Recipe> results = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            //get the recipes array
            JSONArray recipes = jsonObject.getJSONArray("recipes");
            for (int i = 0; i < recipes.length(); i++) {
                JSONObject recipeJson = recipes.getJSONObject(i);


                Recipe recipeObject = new RecipeBuilder()
                        .setId(recipeJson.getInt("id"))
                        .setName(recipeJson.getString("title"))
                        .setImageUrl(recipeJson.getString("image"))
                        .setHealthPoints(Integer.toString(recipeJson.getInt("healthScore")))
                        .setReadyInMinutes(Integer.toString(recipeJson.getInt("readyInMinutes")))
                        .setServings(Integer.toString(recipeJson.getInt("servings")))
                        .setFeatures(RecipeUtil.getRecipeFeatures(recipeJson))
                        .setDishType(RecipeUtil.getDishType(recipeJson))
                        .setIngredients(getIngredientsFromJsonArray(recipeJson.getJSONArray("extendedIngredients")))
                        .setInstructions(getInstructionsFromJsonArray((recipeJson.getJSONArray("analyzedInstructions"))))
                        .createRecipe();

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
                Recipe recipeObject = new RecipeBuilder()
                        .setId(recipeJson.getInt("id"))
                        .setName(recipeJson.getString("title"))
                        .setImageUrl(String.format(apiImagePath, recipeJson.getString("image")))
                        .setReadyInMinutes(Integer.toString(recipeJson.getInt("readyInMinutes")))
                        .setServings(Integer.toString(recipeJson.getInt("servings")))
                        .createRecipe();

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
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Recipe recipe = new RecipeBuilder()
                        .setId(jsonObject.getInt("id"))
                        .setName(jsonObject.getString("title"))
                        .setImageUrl(String.format(apiImagePath, jsonObject.getString("image")))
                        .setReadyInMinutes(Integer.toString(jsonObject.getInt("readyInMinutes")))
                        .setServings(Integer.toString(jsonObject.getInt("servings")))
                        .createRecipe();
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

    static Recipe getRecipeFromJson(@NonNull String jsonString) {
        try {
            JSONObject recipeJson = new JSONObject(jsonString);
            return new RecipeBuilder().setId(recipeJson.getInt("id"))
                    .setName(recipeJson.getString("title"))
                    .setImageUrl(String.format(apiImagePath, recipeJson.getString("image")))
                    .setHealthPoints(Integer.toString(recipeJson.getInt("healthScore")))
                    .setReadyInMinutes(Integer.toString(recipeJson.getInt("readyInMinutes")))
                    .setServings(Integer.toString(recipeJson.getInt("servings")))
                    .setFeatures(RecipeUtil.getRecipeFeatures(recipeJson))
                    .setDishType(RecipeUtil.getDishType(recipeJson))
                    .setIngredients(getIngredientsFromJsonArray(recipeJson.getJSONArray("extendedIngredients")))
                    .setInstructions(getInstructionsFromJsonArray((recipeJson.getJSONArray("analyzedInstructions"))))
                    .createRecipe();
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
