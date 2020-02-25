package com.example.bookapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.bookapp.activities.WelcomeActivity;
import com.example.bookapp.fragments.BottomSheetPromptLogin;
import com.example.bookapp.fragments.ErrorFragment;
import com.example.bookapp.fragments.ExpandedItemFragment;
import com.example.bookapp.fragments.SearchFragment;
import com.example.bookapp.interfaces.MainActivityInterface;
import com.example.bookapp.models.AuthenticationService;
import com.example.bookapp.models.Comment;
import com.example.bookapp.models.NonUploadedPost;
import com.example.bookapp.models.Post;
import com.example.bookapp.models.User;
import com.example.bookapp.models.ViewModelPost;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private ApiManager apiManager;
    private User currentUser;
    private ViewModelPost viewModelPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        configureDefaultVariables();
        if (shouldShowWelcomeActivity()) {
            startWelcomeActivity();
        } else {
            currentUser = getCurrentUser();
            if (AppUtilities.isNetworkAvailable(this)) {
                apiManager = ApiManager.getInstance(this);
                apiManager.setApiManagerDataCallback(this);
                apiManager.pushRequestLatestPosts();
                getFavoritePosts();

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
        viewModelPost = new ViewModelProvider(this).get(ViewModelPost.class);
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        configureNavigationView();

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
        viewModelPost.setLatestPosts(latestPosts);
    }

    @Override
    public void onPostDetailsReady(@NonNull Post post, @Nullable ArrayList<Comment> comments, @Nullable ArrayList<Post> similarPosts) {
        if (viewModelPost.getSavedPosts().getValue().contains(post)) {
            post.setSaved(true);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(ExpandedItemFragment.KEY_EXPANDED_ITEM, post);
        bundle.putParcelableArrayList(ExpandedItemFragment.KEY_COMMENTS, comments);
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.expandedItemFragment, bundle);
    }

    @Override
    public void onPostSearchReady(ArrayList<Post> data) {
//         navController.getBackStack().
//         searchFragment.displaySearchResults(data);
    }

    @Override
    public void onAutocompleteSuggestionsReady(ArrayList<Post> suggestions) {
        viewModelPost.setAutocompleteResults(suggestions);
    }

    @Override
    public void onSavedPostsReady(ArrayList<Post> savedPosts) {
        viewModelPost.setSavedPosts(savedPosts);
    }


    private void configureNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        navController.navigate(R.id.homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home: {
                    navController.navigate(R.id.homeFragment);
                    break;
                }
                case R.id.search_item: {
                    navController.navigate(R.id.searchFragment);
                    break;
                }
                case R.id.saved_items: {
                    navController.navigate(R.id.favoriteItems);
                    break;
                }
            }

            return true;
        });
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
        apiManager.pushRequestAddPostToFavorites(post.getPostID(), currentUser.getUserID());
        viewModelPost.addFavoritePost(post);
    }


    @Override
    public void deleteSavedPost(Post post) {
        viewModelPost.removeFavoritePost(post);
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
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                //     Snackbar.make(findViewById(R.id.nav_graph_main_activity), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

            }
        }
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
