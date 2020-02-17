package com.example.bookapp;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.models.Recipe;

import java.util.ArrayList;
import java.util.HashMap;

public class DataApiManager {
    private Activity activity;
    private RequestQueue requestQueue;
    private DataApiManagerCallback dataApiManagerCallback;
    private static final String URL_RANDOM_RECIPES = "https://api.spoonacular.com/recipes/random?apiKey=8d7003ab81714ae7b9d6e003a61ee0c4&number=1";
    private static final String URL_SEARCH_RECIPES_LIMIT_20 = "https://api.spoonacular.com/recipes/search?query=%s&number=20&apiKey=8d7003ab81714ae7b9d6e003a61ee0c4";
    private static final String URL_SEARCH_RECIPE_ID = "https://api.spoonacular.com/recipes/%s/information?includeNutrition=false&apiKey=8d7003ab81714ae7b9d6e003a61ee0c4";
    private static final String URL_RECIPE_AUTOCOMPLETE = "https://api.spoonacular.com/recipes/autocomplete?number=6&query=%s&apiKey=8d7003ab81714ae7b9d6e003a61ee0c4";
    private static final String URL_SIMILAR_RECIPES = "https://api.spoonacular.com/recipes/%s/similar?apiKey=8d7003ab81714ae7b9d6e003a61ee0c4&number=5";

    public DataApiManager(Activity activity, boolean mockState) {
        this.activity = activity;
        requestQueue = Volley.newRequestQueue(activity);
        dataApiManagerCallback = (DataApiManagerCallback) activity;
    }

    void pushRequestRandomRecipes() {
        //compose the request string to get the random recipes
        StringRequest randomRecipesRequest = new StringRequest(Request.Method.GET, URL_RANDOM_RECIPES,
                (response) -> {
                    //once the data is fetched process it
                    ArrayList<Recipe> processedData = RecipeUtil.getRecipesListFromJson(response);
                    activity.runOnUiThread(() -> dataApiManagerCallback.onRandomRecipesDataReady(processedData));
                }, Throwable::printStackTrace);

        requestQueue.add(randomRecipesRequest);
    }

    void pushRequestGetRecipeDetails(int id) {
        String formattedRecipeURL = String.format(URL_SEARCH_RECIPE_ID, id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, formattedRecipeURL, (response) ->
        {//process response
            Recipe recipe = RecipeUtil.getRecipeFromJson(response);
            if (recipe != null) {
                pushRequestSimilarRecipes(recipe);
            }

        },
                (error) -> {
                });
        requestQueue.add(stringRequest);
    }

    void pushRequestPerformSearch(String query) {
        //build query
        String formattedSearchURL = String.format(URL_SEARCH_RECIPES_LIMIT_20, query);

        //push request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, formattedSearchURL, (response) ->
        {//process response
            ArrayList<Recipe> searchResults = RecipeUtil.getSearchResultsFromJson(response);
            activity.runOnUiThread(() -> dataApiManagerCallback.onRecipeSearchReady(searchResults));
        },
                (error) -> {
                });
        requestQueue.add(stringRequest);
    }

    void pushRequestAutocomplete(String query) {
        String formattedAutocompleteURl = String.format(URL_RECIPE_AUTOCOMPLETE, query);
        //push request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, formattedAutocompleteURl, (response) ->
        {//process response
            HashMap<Integer, String> searchResults = RecipeUtil.getAutocompleteSuggestionFromJson(response);
            activity.runOnUiThread(() -> dataApiManagerCallback.onAutocompleteSuggestionsReady(searchResults));
        },
                (error) -> {
                });
        requestQueue.add(stringRequest);
    }

    void pushRequestSimilarRecipes(Recipe recipe) {
        String formattedAutocompleteURl = String.format(URL_SIMILAR_RECIPES, recipe.getId());
        //push request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, formattedAutocompleteURl, (response) ->
        {
            ArrayList<Recipe> similarRecipes = RecipeUtil.getSimilarRecipesFromJson(response);

            activity.runOnUiThread(() -> dataApiManagerCallback.onRecipeDetailsReady(recipe, similarRecipes));

        },
                (error) -> {
                });
        requestQueue.add(stringRequest);
    }

    public interface DataApiManagerCallback {
        void onRandomRecipesDataReady(ArrayList<Recipe> data);

        void onRecipeDetailsReady(Recipe recipe, ArrayList<Recipe> similarRecipes);

        void onRecipeSearchReady(ArrayList<Recipe> data);

        void onAutocompleteSuggestionsReady(HashMap<Integer, String> data);
    }
}
