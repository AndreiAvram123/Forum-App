package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.Adapters.AdapterRecyclerView;
import com.example.bookapp.fragments.ExpandedItemFragment;
import com.example.bookapp.fragments.MainFragment;
import com.example.bookapp.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * The main activity acts as the controller
 * for the main screen
 */

public class MainActivity extends AppCompatActivity implements AdapterRecyclerView.AdapterInterface, MainFragment.MainFragmentInterface {
    private static final String URL_RANDOM_RECIPES = "https://api.spoonacular.com/recipes/random?apiKey=8d7003ab81714ae7b9d6e003a61ee0c4&number=1";
    private static final String URL_SEARCH_RECIPES_LIMIT_10 = "https://api.spoonacular.com/recipes/search?query=%s&number=10&apiKey=8d7003ab81714ae7b9d6e003a61ee0c4";

    private ArrayList<Recipe> randomRecipes = new ArrayList<>();
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editorSharedPreferences;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        requestQueue = Volley.newRequestQueue(this);
        pushRequestRandomRecipes();
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editorSharedPreferences = sharedPreferences.edit();
    }


    private void pushRequestRandomRecipes() {
        //compose the request string to get the random recipes
        StringRequest randomRecipesRequest = new StringRequest(Request.Method.GET, URL_RANDOM_RECIPES,
                (response) -> {
                    //once the data is fetched process it
                    runOnUiThread(() -> processRequestRandomRecipes(response));
                }, (error) -> error.printStackTrace());
        requestQueue.add(randomRecipesRequest);
    }

    private ArrayList<String> getSearchHistory() {
        Set<String> searchHistorySet = sharedPreferences.getStringSet(getString(R.string.search_history_key ), null);
        ArrayList<String> searchHistoryArrayList = new ArrayList<>();
        if (searchHistorySet != null) {
            searchHistoryArrayList.addAll(searchHistorySet);
            Collections.reverse(searchHistoryArrayList);
            return searchHistoryArrayList;
        }
        searchHistoryArrayList.add("No search history");
        return searchHistoryArrayList;
    }

    private void processRequestRandomRecipes(String response) {
        randomRecipes.addAll(RecipeUtil.getRecipesListFromJson(response));
        mainFragment = MainFragment.getInstance(randomRecipes, getSearchHistory());
        displayMainFragment();

    }

    private void displayMainFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main_activity,mainFragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void insertSearchInDatabase(String search) {
      //todo
        //not done yet
    }

    @Override
    public void performSearch(String query) {
        //build query
        String formattedSearchURL = String.format(URL_SEARCH_RECIPES_LIMIT_10, query);

        //push request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, formattedSearchURL, (response) ->
        {//process response
            mainFragment.displaySearchResults(RecipeUtil.getSearchResultsFromJson(response));
        },
                (error) -> {
                });
        requestQueue.add(stringRequest);

    }

    @Override
    public void expandItem(Recipe recipe) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main_activity, ExpandedItemFragment.getInstance(recipe))
                .addToBackStack(null)
                .commit();
    }
}
