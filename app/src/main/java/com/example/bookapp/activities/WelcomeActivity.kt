package com.example.bookapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.bookapp.R
import com.example.bookapp.fragments.AuthenticationFragment
import com.example.bookapp.models.User
import com.example.bookapp.viewModels.ViewModelUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class WelcomeActivity : AppCompatActivity(), AuthenticationFragment.FragmentCallback {
    private var googleSignInAccount: GoogleSignInAccount? = null
    private val viewModelUser: ViewModelUser by viewModels()
    private val requestCodeGoogleSignIn = 1

    //use share preferences to share data across activities
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_welcome)
        sharedPreferences = getSharedPreferences(getString(R.string.key_preferences), Context.MODE_PRIVATE)

        viewModelUser.user.observe(this, Observer {
            it?.let {
                saveUserInMemory(it)
            }
        })

    }


    private fun SharedPreferences.edit(commit: Boolean = true,
                                       action: SharedPreferences.Editor.() -> Unit) {
        val editor = edit()
        action(editor)
        if (commit) {
            editor.commit()
        } else {
            editor.apply()
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                googleSignInAccount = task.getResult(ApiException::class.java)
                googleSignInAccount?.let {
                    viewModelUser.loginWithGoogle(it.id!!, it.displayName!!, it.email!!)
                }

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                // ...
                e.printStackTrace()
                Snackbar.make(findViewById(R.id.container_activity_welcome), "Authentication Failed for google.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun saveUserInMemory(user: User) {
        sharedPreferences.edit {
            putInt(getString(R.string.key_user_id), user.userID)
            putString(getString(R.string.key_username), user.username)
            putString(getString(R.string.key_email), user.email)
        }
        startMainActivity()
    }


    override fun loginWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build()
        val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent;
        startActivityForResult(signInIntent, requestCodeGoogleSignIn)
    }

}