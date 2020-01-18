package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.fragments.MainFragment;
import com.example.bookapp.fragments.SearchHistoryFragment;
import com.example.bookapp.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * The main activity acts as the controller
 * for the main screen
 */

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout containerMain;
    private SearchView searchView;
    private static final String URL_RANDOM_RECIPES = "https://api.spoonacular.com/recipes/random?apiKey=8d7003ab81714ae7b9d6e003a61ee0c4&number=10";
    private static final String URL_SEARCH_RECIPES_LIMIT_10 = "https://api.spoonacular.com/recipes/search?query=%s&number=10&apiKey=8d7003ab81714ae7b9d6e003a61ee0c4";

    private ArrayList<Recipe> randomRecipes = new ArrayList<>();
    private RequestQueue requestQueue;
    private Set<String> oldSearches = new TreeSet<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editorSharedPreferences;
    private MainFragment randomRecipesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        requestQueue = Volley.newRequestQueue(this);
        pushRequest();
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editorSharedPreferences = sharedPreferences.edit();
    }

    private void getSearchHistory() {
        if (sharedPreferences.getStringSet(getString(R.string.search_history_key), null) == null) {
            oldSearches.add("No search history");
        } else {
            oldSearches =  sharedPreferences.getStringSet(getString(R.string.search_history_key), null);
        }
    }

    private void pushRequest() {
        //compose the request string to get the random recipes
        StringRequest randomRecipesRequest = new StringRequest(Request.Method.GET, URL_RANDOM_RECIPES,
                (response) -> {
                    //once the data is fetched process it
                    runOnUiThread(() -> processRequest(response));
                }, (error) -> error.printStackTrace());
        requestQueue.add(randomRecipesRequest);
    }

    private void processRequest(String response) {
        randomRecipes.addAll(getRecipesFromJson(response));
        randomRecipesFragment = MainFragment.getInstance(randomRecipes);
        displayRandomRecipesFragment();
    }

    private ArrayList<Recipe> getRecipesFromJson(String json) {
        ArrayList<Recipe> results = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            //get the recipes array
            JSONArray recipes = jsonObject.getJSONArray("recipes");
            for (int i = 0; i < recipes.length(); i++) {
                JSONObject recipeJson = recipes.getJSONObject(i);
                Recipe recipeObject = new Recipe(recipeJson.getString("title"),
                        recipeJson.getString("image"));
                results.add(recipeObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    private ArrayList<Recipe> getSearchResultsFromJson(String json){
        ArrayList<Recipe> results = new ArrayList<>();
        String apiImagePath ="https://spoonacular.com/recipeImages/%s";
        try {
            JSONObject jsonObject = new JSONObject(json);
            //get the recipes array
            JSONArray recipes = jsonObject.getJSONArray("results");
            for (int i = 0; i < recipes.length(); i++) {
                JSONObject recipeJson = recipes.getJSONObject(i);
                Recipe recipeObject = new Recipe(recipeJson.getString("title"),
                        String.format(apiImagePath,recipeJson.getString("image")));
                results.add(recipeObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void displayRandomRecipesFragment() {
        if (randomRecipesFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction().
                    replace(R.id.container_main, randomRecipesFragment)
                    .commit();
        }
    }

    private void initializeViews() {
        containerMain = findViewById(R.id.container_main);
        searchView = findViewById(R.id.searchView);
        configureSearch();

    }

    private void configureSearch() {
        searchView.setOnClickListener(view -> {
            if (searchView.isIconified()) {
                searchView.setBackground(getDrawable(R.drawable.search_background_highlighted));
                displayOldSearchList();
                searchView.setIconified(false);
            }

        });
        searchView.setOnCloseListener(() -> {
            searchView.setBackground(getDrawable(R.drawable.search_background));
            displayRandomRecipesFragment();
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.trim().equals("")) {
                    oldSearches.add(query);
                    editorSharedPreferences.putStringSet(getString(R.string.search_history_key), new TreeSet<>(oldSearches));
                    editorSharedPreferences.commit();
                    performSearch(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    /**
     * This method is used in order to perform a
     * search query
     *
     * @param query
     */
    private void performSearch(String query) {
        //build query
        String formattedSearchURL = String.format(URL_SEARCH_RECIPES_LIMIT_10, query);

        //push request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, formattedSearchURL, (response) ->
        {//process response
            displaySearchFragment(getSearchResultsFromJson(response));
        },
                (error) -> {
                });
        requestQueue.add(stringRequest);

    }

    private void displaySearchFragment(ArrayList<Recipe> searchResults) {
        //reuse the RandomRecipesFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_main, MainFragment.getInstance(searchResults))
                .addToBackStack(null)
                .commit();
    }

    private void displayOldSearchList() {
        //display the newest searches first
        getSearchHistory();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container_main, SearchHistoryFragment.getInstance(processSet(oldSearches))).
                addToBackStack(null).commit();
    }
    private ArrayList<String> processSet(Set<String> set){
        ArrayList<String> searches = new ArrayList<>(set);
        Collections.reverse(searches);
        return searches;
    }




}
