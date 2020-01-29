package com.example.bookapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.Adapters.AdapterRecyclerView;
import com.example.bookapp.fragments.BottomSheetPromptLogin;
import com.example.bookapp.fragments.RecipeDataFragment;
import com.example.bookapp.fragments.ExpandedItemFragment;
import com.example.bookapp.fragments.SavedRecipesDataObject;
import com.example.bookapp.fragments.SearchFragment;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**
 * The main activity acts as the controller
 * for the main screen
 */

public class MainActivity extends AppCompatActivity implements
        ActionsInterface,
        BottomSheetPromptLogin.BottomSheetInterface, SearchFragment.SearchFragmentInterface, DataApiManager.DataApiManagerCallback {

    private ArrayList<Recipe> randomRecipes = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private RecipeDataFragment savedRecipesFragment;
    private RecipeDataFragment homeFragment;
    private SearchFragment searchFragment;
    private ArrayList<Recipe> savedRecipes = new ArrayList<>();
    private DataApiManager dataApiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        configureDefaultParameters();
        configureFirebaseParameters();
        savedRecipesFragment = RecipeDataFragment.getInstance(savedRecipes);
        searchFragment = SearchFragment.getInstance(getSearchHistory());
        if (firebaseUser != null) {
            getSavedRecipesForCurrentUser(firebaseUser.getUid());
        } else {
            dataApiManager.pushRequestRandomRecipes();
        }

    }

    private void configureFirebaseParameters() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private void configureDefaultParameters() {
        dataApiManager  = new DataApiManager(this);
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }


    private ArrayList<String> getSearchHistory() {
        Set<String> searchHistorySet = sharedPreferences.getStringSet(getString(R.string.search_history_key), null);
        ArrayList<String> searchHistoryArrayList = new ArrayList<>();
        if (searchHistorySet != null) {
            searchHistoryArrayList.addAll(searchHistorySet);
            Collections.reverse(searchHistoryArrayList);
            return searchHistoryArrayList;
        }
        return searchHistoryArrayList;
    }

//
    @Override
    public void onRandomRecipesDataReady(ArrayList<Recipe> data) {
        randomRecipes.addAll(data);
        checkWhichRandomRecipeIsSaved();
        homeFragment = RecipeDataFragment.getInstance(randomRecipes);
        configureNavigationView();
    }

    @Override
    public void onRecipeDetailsReady(Recipe recipe,ArrayList<Recipe> similarRecipes) {
        if (recipe != null) {
            displayFragmentAddToBackStack(ExpandedItemFragment.getInstance(recipe,similarRecipes));
        }
    }

    @Override
    public void onRecipeSearchReady(ArrayList<Recipe> data) {
        searchFragment.displaySearchResults(data);
    }

    @Override
    public void onAutocompleteSuggestionsReady(HashMap<Integer,String>data) {
        searchFragment.displayFetchedSuggestions(data);
    }

    private void checkWhichRandomRecipeIsSaved() {
        for (Recipe recipe : randomRecipes) {
            if (savedRecipes.contains(recipe)) {
                recipe.setSaved(true);
            }
        }
    }

    private void configureNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //display home fragment
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        displayFragment(homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home: {
                    displayFragment(homeFragment);
                    break;
                }
                case R.id.search_item:{
                    displayFragment(searchFragment);
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



    //todo
    //check for duplicates
    private void insertSearchInDatabase(String search) {
        Set<String> currentSearchHistory = new TreeSet<>(getSearchHistory());
        currentSearchHistory.add(search);
       SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
       sharedPreferencesEditor.putStringSet(getString(R.string.search_history_key),currentSearchHistory);
       sharedPreferencesEditor.apply();

    }

    @Override
    public void performSearch(String query) {

        insertSearchInDatabase(query);
        dataApiManager.pushRequestPerformSearch(query);


    }


    @Override
    public void fetchSuggestions(String query) {
       dataApiManager.pushRequestAutocomplete(query);
    }


    private void displayFragmentAddToBackStack(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main_activity, fragment)
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
        } else {
            savedRecipes.add(recipe);
            updateUserFirebaseDocument();
        }
    }

    @Override
    public void deleteSaveRecipe(Recipe recipe) {
        if (firebaseUser == null) {
            requestLogIn();
        } else {
            savedRecipes.remove(recipe);
            updateUserFirebaseDocument();
        }
    }

    @Override
    public void expandRecipe(Recipe recipe) {
        if (recipe.getIngredients() == null) {
            //get the full data from the api
            dataApiManager.pushRequestGetRecipeDetails(recipe.getId());
        } else {
           dataApiManager.pushRequestSimilarRecipes(recipe);
        }
    }

    private void updateUserFirebaseDocument() {
        savedRecipesFragment = RecipeDataFragment.getInstance(savedRecipes);
        DocumentReference documentReference = firebaseFirestore.collection("users_saved_recipes").document(firebaseUser.getUid());
        documentReference.set(new SavedRecipesDataObject(savedRecipes), SetOptions.merge());
    }

    private void requestLogIn() {
        BottomSheetPromptLogin bottomSheetPromptLogin = BottomSheetPromptLogin.newInstance();
        bottomSheetPromptLogin.show(getSupportFragmentManager(), BottomSheetPromptLogin.TAG);

    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        updateUIWithUserInfo();
    }

    private void updateUIWithUserInfo() {
    }

    private void getSavedRecipesForCurrentUser(String uId) {
        DocumentReference userRecipesReferences = firebaseFirestore.collection("users_saved_recipes").document(uId);
        userRecipesReferences.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    SavedRecipesDataObject savedRecipesDataObject = document.toObject(SavedRecipesDataObject.class);
                    if (savedRecipesDataObject != null) {
                        savedRecipesDataObject.getSavedRecipes().forEach((s, recipe) -> recipe.setSaved(true));
                        savedRecipes.addAll(savedRecipesDataObject.getSavedRecipes().values());
                        savedRecipesFragment = RecipeDataFragment.getInstance(savedRecipes);
                    }
                }
            }
          dataApiManager.pushRequestRandomRecipes();
        }).addOnFailureListener(e -> dataApiManager.pushRequestRandomRecipes());

    }


    @Override
    public void onItemClick(int itemId) {
        switch (itemId) {
            case R.id.sign_in_google_item: {
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
