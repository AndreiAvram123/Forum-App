package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.fragments.RandomRacipesFragment;
import com.example.bookapp.fragments.SearchHistoryFragment;
import com.example.bookapp.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private ArrayList<Recipe> randomRecipes = new ArrayList<>();
    private RequestQueue requestQueue;
    private Set<String> oldSearches = new TreeSet<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editorSharedPreferences;
    private RandomRacipesFragment randomRacipesFragment;
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
            oldSearches= (sharedPreferences.getStringSet(getString(R.string.search_history_key), null));
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
        try {
            JSONObject jsonObject = new JSONObject(response);
            //get the recipes array
            JSONArray recipes = jsonObject.getJSONArray("recipes");
            for (int i = 0; i < recipes.length(); i++) {
                JSONObject recipeJson = recipes.getJSONObject(i);
                Recipe recipeObject = new Recipe(recipeJson.getString("title"),
                        recipeJson.getString("image"));
                randomRecipes.add(recipeObject);
            }
            randomRacipesFragment = RandomRacipesFragment.getInstance(randomRecipes);
            displayRandomRecipesFragment();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayRandomRecipesFragment() {
        if(randomRacipesFragment!=null) {
            getSupportFragmentManager()
                    .beginTransaction().
                    replace(R.id.container_main, randomRacipesFragment)
                    .commit();
        }
    }

    private void initializeViews() {
        containerMain = findViewById(R.id.container_main);
        searchView = findViewById(R.id.searchView);
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
                if (query.trim() != "") {
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
     * @param query
     */
    private void performSearch(String query) {

    }

    private void displayOldSearchList() {
        getSearchHistory();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container_main, SearchHistoryFragment.getInstance(new ArrayList<>(oldSearches))).
                addToBackStack(null).commit();
    }


}
