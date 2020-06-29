package com.socialMedia.bookapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.socialMedia.bookapp.R
import com.socialMedia.bookapp.fragments.LoginFragment
import com.socialMedia.dataLayer.dataMappers.toUser
import com.socialMedia.dataLayer.interfaces.dao.UserDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

const val requestCodeGoogleSignIn = 10

@AndroidEntryPoint
@InternalCoroutinesApi
class WelcomeActivity : AppCompatActivity(), LoginFragment.FragmentCallback {

    private var googleSignInAccount: GoogleSignInAccount? = null


    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser
        if (user != null) {
            startMainActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_welcome)


    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == requestCodeGoogleSignIn) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                googleSignInAccount = task.getResult(ApiException::class.java)

                googleSignInAccount?.let {
                    signInWithFirebaseGoogle(it.idToken!!)
                }

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                // ...
                e.printStackTrace()
                Snackbar.make(findViewById(R.id.container_activity_welcome), "Authentication Failed for google.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithFirebaseGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = firebaseAuth.currentUser!!.toUser()
                lifecycleScope.launch {
                    userDao.insertUser(user)
                    runOnUiThread {
                        startMainActivity()
                    }
                }
            } else {
                Snackbar.make(findViewById(R.id.container_activity_welcome), "Authentication failed", Snackbar.LENGTH_LONG).show()
            }
        }
    }


    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun loginWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()
        val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent;
        startActivityForResult(signInIntent, requestCodeGoogleSignIn)
    }

}