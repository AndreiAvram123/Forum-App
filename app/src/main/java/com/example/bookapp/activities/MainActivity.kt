package com.example.bookapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.bookapp.AppUtilities
import com.example.bookapp.R
import com.example.bookapp.api.ApiManager
import com.example.bookapp.fragments.BottomSheetPromptLogin
import com.example.bookapp.fragments.BottomSheetPromptLogin.BottomSheetInterface
import com.example.bookapp.fragments.ErrorFragment.ErrorFragmentInterface
import com.example.bookapp.fragments.ExpandedItemFragmentDirections
import com.example.bookapp.fragments.FriendsFragmentDirections
import com.example.bookapp.interfaces.MainActivityInterface
import com.example.bookapp.interfaces.MessageInterface
import com.example.bookapp.models.*
import com.example.bookapp.viewModels.ViewModelFriends
import com.example.bookapp.viewModels.ViewModelMessages
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.repositories.MessageRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

/**
 * The main activity acts as the controller
 * for the main screen
 */
class MainActivity : AppCompatActivity(), MainActivityInterface, BottomSheetInterface, ErrorFragmentInterface, MessageInterface {
    private var sharedPreferences: SharedPreferences? = null
    private var apiManager: ApiManager? = null
    private val viewModelPost: ViewModelPost by viewModels()
    private val viewModelFriends: ViewModelFriends by viewModels()
    private val viewModelUser: ViewModelUser by viewModels()
    private val viewModelMessages: ViewModelMessages by viewModels()
    private var requestQueue: RequestQueue? = null
    private val messageRepository: MessageRepository by lazy {
        MessageRepository.getInstance(requestQueue, viewModelUser.user.value!!.userID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main_activity)
        configureDefaultVariables()
        currentUser
        if (shouldShowWelcomeActivity()) {
            startWelcomeActivity()
        } else {
            if (AppUtilities.isNetworkAvailable(this)) {
                configureApiManager()
            }
        }
    }

    private fun configureApiManager() {
        apiManager = ApiManager.getInstance(this)
        messageRepository.setCallback(viewModelMessages)
    }

    private val currentUser: Unit
        private get() {
            val userID = sharedPreferences!!.getString(getString(R.string.key_user_id), null)
            val username = sharedPreferences!!.getString(getString(R.string.key_username), null)
            if (userID != null && username != null) {
                val builder = User.Builder()
                builder.setUserID(userID)
                builder.setUsername(username)
                viewModelUser!!.setUser(builder.create())
            } else {
                startWelcomeActivity()
            }
        }

    private fun shouldShowWelcomeActivity(): Boolean {
        return !sharedPreferences!!.getBoolean(getString(R.string.welcome_activity_shown_key), false)
    }

    private fun startWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun configureDefaultVariables() {
        requestQueue = Volley.newRequestQueue(this)
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        configureNavigationView()
    }


    private val searchHistory: ArrayList<String?>
        private get() {
            val searchHistorySet = sharedPreferences!!.getStringSet(getString(R.string.search_history_key), null)
            val searchHistoryArrayList = ArrayList<String?>()
            if (searchHistorySet != null) {
                searchHistoryArrayList.addAll(searchHistorySet)
                Collections.reverse(searchHistoryArrayList)
                return searchHistoryArrayList
            }
            return searchHistoryArrayList
        }

    private fun configureNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController
        navController.navigate(R.id.homeFragment)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.homeFragment)
                }
                R.id.search_item -> {
                    navController.navigate(R.id.searchFragment)
                }
                R.id.saved_items -> {
                    navController.navigate(R.id.favoriteItems)
                }
                R.id.my_posts_item -> {
                    navController.navigate(R.id.fragmentMyPosts)
                }
                R.id.friends_item -> {
                    navController.navigate(R.id.friendsFragment)
                }
            }
            true
        }
    }

    override fun sharePost(post: Post) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        sendIntent.type = "text/plain"
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun savePost(post: Post) { //      apiManager.pushRequestAddPostToFavorites(post.getPostID(), viewModelUser.getUser().getValue().getUserID());
//        viewModelPost.addFavoritePost(post);
    }

    override fun deleteSavedPost(post: Post) {}
    override fun expandPost(postID: Int) {
        val action: NavDirections = ExpandedItemFragmentDirections.actionGlobalExpandedItemFragment(postID.toLong())
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(action)
    }

    override fun uploadComment(comment: Comment) {
        apiManager!!.uploadNewComment(comment, viewModelUser!!.user.value!!.userID)
    }

    override fun uploadPost(nonUploadedPost: NonUploadedPost) {
        apiManager!!.uploadPost(nonUploadedPost, viewModelUser!!.user.value!!.userID)
    }

    override fun startChat(userID: String) {
        messageRepository!!.pushRequestFetchOldMessages(userID, 0)
        val action: NavDirections = FriendsFragmentDirections.actionFriendsFragmentToMessagesFragment(viewModelUser!!.user.value!!.userID, userID)
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(action)
    }

    override fun fetchNewPosts() {}
    /**
     * Use this method in order to display a bottom
     * sheet for the user to select a login option
     */
    private fun requestLogIn() {
        val bottomSheetPromptLogin = BottomSheetPromptLogin.newInstance()
        bottomSheetPromptLogin.show(supportFragmentManager, BottomSheetPromptLogin.TAG)
    }

    override fun onBottomSheetItemClicked(itemId: Int) {
        if (itemId == R.id.login_with_google_item) {
            startActivityForResult(AuthenticationService.getInstance().getGoogleSignInIntent(this), 1)
        }
        if (itemId == R.id.login_other_options_item) {
            startWelcomeActivity()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try { // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
            } catch (e: ApiException) { // Google Sign In failed, update UI appropriately
// ...
//     Snackbar.make(findViewById(R.id.nav_graph_main_activity), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    override fun refreshErrorState(error: String) {
        if (error == getString(R.string.no_internet_connection)) {
            if (AppUtilities.isNetworkAvailable(this)) { //   apiManager.pushRequestLatestPosts();
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        messageRepository!!.shutdownAsyncTasks()
    }

    override fun sendMessage(messageContent: String, user2ID: String, currentUserID: String) {
        messageRepository!!.sendMessage(messageContent, user2ID, currentUserID)
    }

    override fun fetchMoreMessages(user2ID: String, offset: Int) {
        messageRepository!!.pushRequestFetchOldMessages(user2ID, offset)
    }
}