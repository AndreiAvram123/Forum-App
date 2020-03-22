package com.example.bookapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.bookapp.R
import com.example.bookapp.fragments.AuthenticationOptionsFragment
import com.example.bookapp.models.ApiConstants
import com.example.bookapp.models.User
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.models.UserDTO
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar

class WelcomeActivity : AppCompatActivity() {
    private var googleSignInAccount: GoogleSignInAccount? = null
    private val viewModelUser: ViewModelUser by viewModels()
    private  val requestCodeGoogleSignIn = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_welcome)
        displayAuthenticationOptionsFragment()
        setFlatWelcomeActivityShown()
        viewModelUser.errorResponseCode.observe(this, Observer {
            if (it == ApiConstants.RESPONSE_CODE_ACCOUNT_UNEXISTENT) {
                createAccount()
            }
        })
        viewModelUser.user.observe(this, Observer {
            it?.let {
                saveUserUserInMemory(it)
            }
        })

    }

    private fun createAccount() {
        googleSignInAccount?.let {
            val userDTO: UserDTO = UserDTO(userID = it.id!!, username = it.displayName!!, email = it.email, profilePicture = it.photoUrl.toString())
            viewModelUser.createThirdPartyAccount(userDTO)
        }
    }

    private fun setFlatWelcomeActivityShown() {
        val sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(getString(R.string.welcome_activity_shown_key), true)
        editor.apply()
    }

    private fun displayAuthenticationOptionsFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_activity_welcome, AuthenticationOptionsFragment.newInstance())
                .addToBackStack(null)
                .commit()
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                googleSignInAccount = task.getResult(ApiException::class.java)
                googleSignInAccount?.email?.let {
                    viewModelUser.getUserFromThirdPartyEmailAccount(it)
                }

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                // ...
                Snackbar.make(findViewById(R.id.container_activity_welcome), "Authentication Failed for google.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun saveUserUserInMemory(userDTO: User) {
        val editor = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit()
        editor.putString(getString(R.string.key_username), userDTO.username)
        editor.putString(getString(R.string.key_user_id), userDTO.userID)
        editor.apply()
        startMainActivity()
    }

}