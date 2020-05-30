package com.example.bookapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.bookapp.R
import com.example.bookapp.fragments.BottomSheetPromptLogin
import com.example.bookapp.fragments.BottomSheetPromptLogin.BottomSheetInterface
import com.example.bookapp.interfaces.MainActivityInterface
import com.example.bookapp.models.AuthenticationService
import com.example.bookapp.models.User
import com.example.bookapp.viewModels.ViewModelChat
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
class MainActivity : AppCompatActivity(), MainActivityInterface, BottomSheetInterface {
    private var sharedPreferences: SharedPreferences? = null
    private val viewModelPost: ViewModelPost by viewModels()
    private val viewModelUser: ViewModelUser by viewModels()
    private val viewModelChat: ViewModelChat by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main_activity)
        configureDefaultVariables()

        currentUser
    }


    private val currentUser: Unit
        get() {
            val userID = sharedPreferences!!.getInt(getString(R.string.key_user_id), 0)
            val email = sharedPreferences!!.getString(getString(R.string.key_email), null)
            if (userID != 0 && email != null) {
                viewModelUser.user.value = (User(userID = userID, username = "username", email = email))
                viewModelPost.user.value = User(userID = userID, username = "username", email = email)
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
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        configureNavigationView()
    }


    private val searchHistory: ArrayList<String?>
        get() {
            val searchHistorySet = sharedPreferences!!.getStringSet(getString(R.string.search_history_key), null)
            val searchHistoryArrayList = ArrayList<String?>()
            if (searchHistorySet != null) {
                searchHistoryArrayList.addAll(searchHistorySet)
                searchHistoryArrayList.reverse()
                return searchHistoryArrayList
            }
            return searchHistoryArrayList
        }

    private fun configureNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController

        NavigationUI.setupWithNavController(bottomNavigationView,
                navController)

    }


    override fun startChat(userID: String) {
        //  val action: NavDirections = FriendsFragmentDirections.actionFriendsFragmentToMessagesFragment(viewModelUser.user.value!!.userID, userID)

        //  Navigation.findNavController(this, R.id.nav_host_fragment).navigate(action)
    }

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


}