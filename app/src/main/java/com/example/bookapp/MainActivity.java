package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.Adapters.AdapterRecyclerView;
import com.example.bookapp.fragments.BottomSheetPromptLogin;
import com.example.bookapp.fragments.DataFragment;
import com.example.bookapp.fragments.ExpandedItemFragment;
import com.example.bookapp.fragments.HomeFragment;
import com.example.bookapp.fragments.SavedRecipesObject;
import com.example.bookapp.interfaces.ActionsInterface;
import com.example.bookapp.models.Recipe;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.ArrayListMultimap;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The main activity acts as the controller
 * for the main screen
 */

public class MainActivity extends AppCompatActivity implements
        AdapterRecyclerView.AdapterInterface, ActionsInterface, HomeFragment.MainFragmentInterface,
        BottomSheetPromptLogin.BottomSheetInterface {
    private static final String URL_RANDOM_RECIPES = "https://api.spoonacular.com/recipes/random?apiKey=8d7003ab81714ae7b9d6e003a61ee0c4&number=1";
    private static final String URL_SEARCH_RECIPES_LIMIT_10 = "https://api.spoonacular.com/recipes/search?query=%s&number=10&apiKey=8d7003ab81714ae7b9d6e003a61ee0c4";

    private ArrayList<Recipe> randomRecipes = new ArrayList<>();
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private HomeFragment homeFragment;
    private BottomNavigationView bottomNavigationView;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private SavedRecipesObject savedRecipesObject = new SavedRecipesObject();
    private DataFragment savedRecipesFragment = DataFragment.getInstance(new ArrayList<>());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        requestQueue = Volley.newRequestQueue(this);
        pushRequestRandomRecipes();
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

    }

    private void initializeLocalDatabase() {
//        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
//                AppDatabase.class, "app-database").build();

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
        Set<String> searchHistorySet = sharedPreferences.getStringSet(getString(R.string.search_history_key), null);
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
        homeFragment = HomeFragment.getInstance(randomRecipes, getSearchHistory());
        configureNavigationView();

    }

    private void configureNavigationView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //display home fragment
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        displayFragment(homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home: {
                    displayFragment(homeFragment);
                    break;
                }
                case R.id.saved_items: {
                    displayFragment(savedRecipesFragment);
                    break;
                }
            }

            return true;
        });
    }

    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main_activity, fragment)
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
            homeFragment.displaySearchResults(RecipeUtil.getSearchResultsFromJson(response));
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

    @Override
    public void shareRecipe(Recipe recipe) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }


    @Override
    public void saveRecipe(Recipe recipe) {
        if (firebaseUser == null) {
            requestLogIn();
        }else{
           savedRecipesObject.addRecipe(recipe);
           DocumentReference documentReference= firebaseFirestore.collection("users_saved_recipes").document(firebaseUser.getUid());
           documentReference.set(savedRecipesObject,SetOptions.merge());
        }
    }

    private void requestLogIn() {
        BottomSheetPromptLogin bottomSheetPromptLogin = BottomSheetPromptLogin.newInstance();
        bottomSheetPromptLogin.show(getSupportFragmentManager(), BottomSheetPromptLogin.TAG);


    }


    @Override
    protected void onStart() {
        super.onStart();
        initializeLocalDatabase();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if(firebaseUser!=null){
            getSavedRecipesFromUser(firebaseUser.getUid());
        }
    }

    private void getSavedRecipesFromUser(String uId) {
        DocumentReference userRecipesReferences = firebaseFirestore.collection("users_saved_recipes").document(uId);
        userRecipesReferences.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    SavedRecipesObject savedRecipesObject = document.toObject(SavedRecipesObject.class);

                    savedRecipesFragment = DataFragment.getInstance(new ArrayList<>(savedRecipesObject.getSavedRecipes().values()));

                } else {

                }
            } else {

            }
        });
    }


    @Override
    public void onItemClick(int itemId) {
        switch (itemId){
            case R.id.sign_in_google_item:{
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 1);
                break;
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Snackbar.make(findViewById(R.id.container_main_activity), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        firebaseUser = firebaseAuth.getCurrentUser();
                    } else {
                        // If sign in fails, display a message to the user.
                        Snackbar.make(findViewById(R.id.container_main_activity), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                    }

                    // ...
                });
    }

}
