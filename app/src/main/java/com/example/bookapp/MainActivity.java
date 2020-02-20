package com.example.bookapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bookapp.activities.WelcomeActivity;
import com.example.bookapp.fragments.BottomSheetPromptLogin;
import com.example.bookapp.fragments.ErrorFragment;
import com.example.bookapp.fragments.ExpandedItemFragment;
import com.example.bookapp.fragments.RecipeDataFragment;
import com.example.bookapp.fragments.SearchFragment;
import com.example.bookapp.interfaces.ActionsInterface;
import com.example.bookapp.models.AuthenticationService;
import com.example.bookapp.models.Comment;
import com.example.bookapp.models.Post;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

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
        BottomSheetPromptLogin.BottomSheetInterface, SearchFragment.SearchFragmentInterface,
        DataApiManager.DataApiManagerCallback
        , ErrorFragment.ErrorFragmentInterface {

    private ArrayList<Post> randomRecipes = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private RecipeDataFragment savedRecipesFragment;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private ArrayList<Post> savedPosts = new ArrayList<>();
    private DataApiManager dataApiManager;
    private ExpandedItemFragment currentExpandedItemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        configureDefaultParameters();
        if (shouldShowWelcomeActivity()) {
            startWelcomeActivity();
        } else {
            initializeFragments();
            configureDatabaseParameters();
            if (AppUtilities.isNetworkAvailable(this)) {
                //dataApiManager.pushRequestRandomRecipes();
                dataApiManager.pushRequestLatestPosts();
            } else {
                displayFragment(ErrorFragment.getInstance(getString(R.string.no_internet_connection), R.drawable.ic_no_wifi));
            }
        }

    }


    private boolean shouldShowWelcomeActivity() {

        return !sharedPreferences.getBoolean(getString(R.string.welcome_activity_shown_key), false);

    }

    private void startWelcomeActivity() {
        markWelcomeActivityAsShown();
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void markWelcomeActivityAsShown() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.welcome_activity_shown_key), true);
        editor.apply();
    }


    private void initializeFragments() {
        //todo
        //modify this
        // savedRecipesFragment = RecipeDataFragment.getInstance(savedPosts);
        searchFragment = SearchFragment.getInstance(getSearchHistory());
    }

    private void configureDatabaseParameters() {
        dataApiManager = new DataApiManager(this);
        //NukeSSLCerts.nuke();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void configureDefaultParameters() {
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
    public void onLatestPostsDataReady(ArrayList<Post> latestPosts) {
        //todo
        //modity
        //randomRecipes.addAll(data);
        //checkWhichRandomRecipeIsSaved();
        homeFragment = HomeFragment.getInstance(latestPosts);
        configureNavigationView();
    }

    @Override
    public void onPostDetailsReady(@NonNull Post post, @Nullable ArrayList<Comment> comments, @Nullable ArrayList<Post> similarPosts) {
        displayFragmentAddToBackStack(ExpandedItemFragment.getInstance(post, comments));

    }

    @Override
    public void onPostSearchReady(ArrayList<Post> data) {
        searchFragment.displaySearchResults(data);
    }

    @Override
    public void onAutocompleteSuggestionsReady(ArrayList<Post> data) {
        searchFragment.displayFetchedSuggestions(data);
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
                case R.id.search_item: {
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


    private void insertSearchInDatabase(String search) {
        Set<String> currentSearchHistory = new TreeSet<>(getSearchHistory());
        currentSearchHistory.add(search);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putStringSet(getString(R.string.search_history_key), currentSearchHistory);
        sharedPreferencesEditor.apply();

    }

    @Override
    public void performSearch(String query) {
        insertSearchInDatabase(query);
    }


    @Override
    public void fetchSuggestions(String query) {
        dataApiManager.pushRequestAutocomplete(query);
    }


    private void displayFragmentAddToBackStack(Fragment fragment) {
        if (fragment instanceof ExpandedItemFragment) {
            currentExpandedItemFragment = (ExpandedItemFragment) fragment;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main_activity, fragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void sharePost(Post post) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }


    @Override
    public void savePost(Post post) {
        if (firebaseUser == null) {
            requestLogIn();
        } else {
            savedPosts.add(post);
            updateUserFirebaseDocument();
            //check if we need to update the UI for the expandedItemFragment

            if (currentExpandedItemFragment != null) {
                currentExpandedItemFragment.informUserPostAddedToFavorites();
            }
        }
    }

    @Override
    public void deleteSavedPost(Post post) {
        savedPosts.remove(post);
        updateUserFirebaseDocument();

        if (currentExpandedItemFragment != null) {
            currentExpandedItemFragment.informUserPostRemovedFromFavorites();

        }
    }

    @Override
    public void expandPost(int postID) {

        dataApiManager.pushRequestGetPostDetails(postID);
    }

    private void updateUserFirebaseDocument() {
        //todo
        //modif


    }

    /**
     * Use this method in order to display a bottom
     * sheet for the user to select a login option
     */
    private void requestLogIn() {
        BottomSheetPromptLogin bottomSheetPromptLogin = BottomSheetPromptLogin.newInstance();
        bottomSheetPromptLogin.show(getSupportFragmentManager(), BottomSheetPromptLogin.TAG);

    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseUser = firebaseAuth.getCurrentUser();
            if (AppUtilities.isNetworkAvailable(this)) {
                getSavedPostsForCurrentUser(firebaseUser);
            }
            updateUIWithUserInfo();
        }
    }

    private void getSavedPostsForCurrentUser(FirebaseUser firebaseUser) {
        //todo
        //needs implementing
    }

    private void updateUIWithUserInfo() {
    }



    @Override
    public void onBottomSheetItemClicked(int itemId) {
        if (itemId == R.id.login_with_google_item) {
            startActivityForResult(AuthenticationService.getInstance().getGoogleSignInIntent(this), 1);
        }
        if (itemId == R.id.login_other_options_item) {
            startWelcomeActivity();
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


    @Override
    public void refreshErrorState(String error) {
        if (error.equals(getString(R.string.no_internet_connection))) {
            if (AppUtilities.isNetworkAvailable(this)) {

                dataApiManager.pushRequestLatestPosts();
                //todo
                //getSavedPostsForCurrentUser(firebaseUser);
            }
        }
    }

}
