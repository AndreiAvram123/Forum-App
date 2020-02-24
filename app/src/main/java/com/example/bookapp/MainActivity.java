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
import com.example.bookapp.fragments.HomeFragment;
import com.example.bookapp.fragments.PostsDataFragment;
import com.example.bookapp.fragments.SearchFragment;
import com.example.bookapp.interfaces.MainActivityInterface;
import com.example.bookapp.models.AuthenticationService;
import com.example.bookapp.models.Comment;
import com.example.bookapp.models.NonUploadedPost;
import com.example.bookapp.models.Post;
import com.example.bookapp.models.User;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * The main activity acts as the controller
 * for the main screen
 */

public class MainActivity extends AppCompatActivity implements
        MainActivityInterface,
        BottomSheetPromptLogin.BottomSheetInterface, SearchFragment.SearchFragmentInterface,
        ApiManager.ApiManagerDataCallback
        , ErrorFragment.ErrorFragmentInterface {

    private SharedPreferences sharedPreferences;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private ArrayList<Post> savedPosts = new ArrayList<>();
    private PostsDataFragment savedPostsFragment;
    private ApiManager apiManager;
    private ExpandedItemFragment currentExpandedItemFragment;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        configureDefaultVariables();
        if (shouldShowWelcomeActivity()) {
            startWelcomeActivity();
        } else {
            currentUser = getCurrentUser();
            searchFragment = SearchFragment.getInstance(getSearchHistory());
            if (AppUtilities.isNetworkAvailable(this)) {
                apiManager = ApiManager.getInstance(this);
                apiManager.setApiManagerDataCallback(this);
                apiManager.pushRequestLatestPosts();
                getFavoritePosts();

            } else {
                displayFragment(ErrorFragment.getInstance(getString(R.string.no_internet_connection), R.drawable.ic_no_wifi));
            }
        }

    }


    private void getFavoritePosts() {
        if (currentUser != null) {
            apiManager.pushRequestGetFavoritePosts(currentUser.getUserID());
        }
    }

    private User getCurrentUser() {
        String userID = sharedPreferences.getString(getString(R.string.key_user_id), null);
        String username = sharedPreferences.getString(getString(R.string.key_username), null);
        if (userID != null && username != null) {
            User.Builder builder = new User.Builder();
            builder.setUserID(userID);
            builder.setUsername(username);
            return builder.createUser();
        }
        return null;
    }


    private boolean shouldShowWelcomeActivity() {

        return !sharedPreferences.getBoolean(getString(R.string.welcome_activity_shown_key), false);

    }

    private void startWelcomeActivity() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    private void configureDefaultVariables() {
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        savedPostsFragment = PostsDataFragment.getInstance(new ArrayList<>());
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

    @Override
    public void onSavedPostsReady(ArrayList<Post> savedPosts) {
        savedPostsFragment = PostsDataFragment.getInstance(savedPosts);
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
                    displayFragment(savedPostsFragment);
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
    public void performSearch(String query) {
    }


    @Override
    public void fetchSuggestions(String query) {
        apiManager.pushRequestAutocomplete(query);
    }

    @Override
    public void fetchSelectedPostById(int id) {
        apiManager.pushRequestGetPostDetails(id);
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
        addPostToSavedFragment(post);
        apiManager.pushRequestAddPostToFavorites(post.getPostID(), currentUser.getUserID());
        //check if we need to
        // .update the UI for the expandedItemFragment

        if (currentExpandedItemFragment != null) {
            currentExpandedItemFragment.informUserPostAddedToFavorites();
        }

    }

    private void addPostToSavedFragment(Post post) {
        savedPosts.add(post);
        if (savedPosts.size() == 1) {
            savedPostsFragment = PostsDataFragment.getInstance(savedPosts);
        } else {
            savedPostsFragment.addNewSavedPost(post);
        }
    }

    @Override
    public void deleteSavedPost(Post post) {
        savedPosts.remove(post);
        savedPostsFragment.removePost(post);
        updateUserFirebaseDocument();

        if (currentExpandedItemFragment != null) {
            currentExpandedItemFragment.informUserPostRemovedFromFavorites();

        }
    }

    @Override
    public void expandPost(int postID) {

        apiManager.pushRequestGetPostDetails(postID);
    }

    @Override
    public void uploadComment(Comment comment) {
        apiManager.uploadNewComment(comment, currentUser.getUserID());

    }

    @Override
    public void uploadPost(NonUploadedPost nonUploadedPost) {
        apiManager.uploadPost(nonUploadedPost, currentUser.getUserID());
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

    }

    private void getSavedPostsForCurrentUser(FirebaseUser firebaseUser) {
        //todo
        //needs implementing
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

                apiManager.pushRequestLatestPosts();
                //todo
                //getSavedPostsForCurrentUser(firebaseUser);
            }
        }
    }


}
