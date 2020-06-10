package com.example.bookapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.bookapp.R
import com.example.bookapp.dagger.*
import com.example.bookapp.models.User
import com.example.bookapp.viewModels.ViewModelChat
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.messages_fragment.*
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModelPost: ViewModelPost by viewModels()
    private val viewModelUser: ViewModelUser by viewModels()
    private val viewModelChat: ViewModelChat by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences(getString(R.string.key_preferences), Context.MODE_PRIVATE)
        val user = getCurrentUser()

        if (user != null) {
            startDagger(user)
            super.onCreate(savedInstanceState)
            setContentView(R.layout.layout_main_activity)
            configureNavigationView()
        } else {
            startWelcomeActivity()
        }

    }

    private fun startDagger(user: User) {
        (application as MyApplication).appComponent = DaggerAppComponent.factory().create(applicationContext, viewModelPost.viewModelScope, user)
        val appComponent = (application as MyApplication).appComponent


        viewModelChat.user.value = user


        appComponent.inject(this)
        appComponent.inject(viewModelPost)
        appComponent.inject(viewModelUser)
        appComponent.inject(viewModelChat)


    }


    private fun getCurrentUser(): User? {
        val userID = sharedPreferences.getInt(getString(R.string.key_user_id), 0)
        if (userID > 0) {
            return User(userID = userID,
                    username = sharedPreferences.getStringNotNull(R.string.key_email),
                    email = sharedPreferences.getStringNotNull(R.string.key_username),
                    profilePicture = "")
        }
        return null
    }


    private fun startWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }


    private fun configureNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController

        NavigationUI.setupWithNavController(bottomNavigationView,
                navController)

    }


    fun SharedPreferences.edit(commit: Boolean = true,
                               action: SharedPreferences.Editor.() -> Unit) {

        val editor = edit()
        action(editor)
        if (commit) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    private fun SharedPreferences.getStringNotNull(keyID: Int
    ): String {
        val value = getString(getString(keyID), "unknown")
        value?.let { return it }
        return "Unknown"
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