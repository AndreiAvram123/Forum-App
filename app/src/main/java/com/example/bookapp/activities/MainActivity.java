package com.example.bookapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.R;
import com.example.bookapp.api.ApiManager;
import com.example.bookapp.api.MessageRepository;
import com.example.bookapp.fragments.BottomSheetPromptLogin;
import com.example.bookapp.fragments.ErrorFragment;
import com.example.bookapp.fragments.ExpandedItemFragmentDirections;
import com.example.bookapp.fragments.FriendsFragmentDirections;
import com.example.bookapp.fragments.SearchFragment;
import com.example.bookapp.interfaces.MainActivityInterface;
import com.example.bookapp.interfaces.MessageInterface;
import com.example.bookapp.models.AuthenticationService;
import com.example.bookapp.models.Comment;
import com.example.bookapp.models.NonUploadedPost;
import com.example.bookapp.models.Post;
import com.example.bookapp.models.User;
import com.example.bookapp.viewModels.ViewModelFriends;
import com.example.bookapp.viewModels.ViewModelMessages;
import com.example.bookapp.viewModels.ViewModelPost;
import com.example.bookapp.viewModels.ViewModelUser;
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
        BottomSheetPromptLogin.BottomSheetInterface, SearchFragment.SearchFragmentInterface
        , ErrorFragment.ErrorFragmentInterface, MessageInterface {

    private SharedPreferences sharedPreferences;
    private ApiManager apiManager;
    private ViewModelPost viewModelPost;
    private ViewModelFriends viewModelFriends;
    private ViewModelUser viewModelUser;
    private ViewModelMessages viewModelMessages;
    private RequestQueue requestQueue;
    private MessageRepository messageRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        configureDefaultVariables();
        getCurrentUser();
        if (shouldShowWelcomeActivity()) {
            startWelcomeActivity();
        } else {
            if (AppUtilities.isNetworkAvailable(this)) {
                configureApiManager();
            }

        }

    }

    private void configureApiManager() {
        apiManager = ApiManager.getInstance(this);
        apiManager.setPostDataCallback(viewModelPost);
        messageRepository = MessageRepository.getInstance(requestQueue, viewModelUser.getUser().getValue().getUserID());
        messageRepository.setCallback(viewModelMessages);
        pushDefaultRequests();
    }

    private void pushDefaultRequests() {
        // apiManager.pushRequestLatestPosts();
        if (viewModelUser.getUser().getValue() != null) {
            apiManager.pushRequestMyPosts(viewModelUser.getUser().getValue().getUserID());
            apiManager.pushRequestGetFavoritePosts(viewModelUser.getUser().getValue().getUserID());
            viewModelFriends.getFriends(viewModelUser.getUser().getValue().getUserID());
        }
    }


    private void getCurrentUser() {
        String userID = sharedPreferences.getString(getString(R.string.key_user_id), null);
        String username = sharedPreferences.getString(getString(R.string.key_username), null);
        if (userID != null && username != null) {
            User.Builder builder = new User.Builder();
            builder.setUserID(userID);
            builder.setUsername(username);
            viewModelUser.setUser(builder.create());
        } else {
            startWelcomeActivity();
        }
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
        requestQueue = Volley.newRequestQueue(this);
        configureViewModels();
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        configureNavigationView();

    }

    private void configureViewModels() {
        viewModelPost = new ViewModelProvider(this).get(ViewModelPost.class);
        viewModelFriends = new ViewModelProvider(this).get(ViewModelFriends.class);
        viewModelMessages = new ViewModelProvider(this).get(ViewModelMessages.class);
        viewModelUser = new ViewModelProvider(this).get(ViewModelUser.class);

//        viewModelPost.getCurrentPost().observe(this, post -> {
//            if (viewModelPost.getSavedPosts().getValue().contains(post)) {
//                post.setSaved(true);
//            }
//            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.expandedItemFragment);
//        });
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
                case R.id.my_posts_item: {
                    navController.navigate(R.id.fragmentMyPosts);
                    break;
                }
                case R.id.friends_item: {
                    navController.navigate(R.id.friendsFragment);
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
        apiManager.pushRequestAddPostToFavorites(post.getPostID(), viewModelUser.getUser().getValue().getUserID());
        viewModelPost.addFavoritePost(post);
    }


    @Override
    public void deleteSavedPost(Post post) {
        viewModelPost.removeFavoritePost(post);
    }

    @Override
    public void expandPost(int postID) {
        NavDirections action = ExpandedItemFragmentDirections.actionGlobalExpandedItemFragment(postID);
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(action);

    }

    @Override
    public void uploadComment(Comment comment) {
        apiManager.uploadNewComment(comment, viewModelUser.getUser().getValue().getUserID());
    }

    @Override
    public void uploadPost(NonUploadedPost nonUploadedPost) {
        apiManager.uploadPost(nonUploadedPost, viewModelUser.getUser().getValue().getUserID());
    }

    @Override
    public void startChat(String userID) {
        messageRepository.pushRequestFetchOldMessages(userID, 0);
        NavDirections action = FriendsFragmentDirections.actionFriendsFragmentToMessagesFragment(viewModelUser.getUser().getValue().getUserID(), userID);
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(action);
    }

    @Override
    public void fetchNewPosts() {
        apiManager.pushRequestGetMorePosts();
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

                //   apiManager.pushRequestLatestPosts();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        messageRepository.shutdownAsyncTasks();
    }

    @Override
    public void sendMessage(@NonNull String messageContent, @NonNull String user2ID, String currentUserID) {
        messageRepository.sendMessage(messageContent, user2ID, currentUserID);
    }

    @Override
    public void fetchMoreMessages(@NonNull String user2ID, int offset) {
        messageRepository.pushRequestFetchOldMessages(user2ID, offset);
    }
}
