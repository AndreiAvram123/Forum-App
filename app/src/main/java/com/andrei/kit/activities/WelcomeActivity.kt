package com.andrei.kit.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.andrei.dataLayer.engineUtils.Status
import com.andrei.kit.R
import com.andrei.kit.fragments.LoginFragment
import com.andrei.kit.user.UserAccountManager
import com.andrei.kit.utils.reObserve
import com.andrei.kit.viewModels.ViewModelUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val requestCodeGoogleSignIn = 1

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity(), LoginFragment.FragmentCallback {

    //todo
    private val viewModelUser: ViewModelUser by viewModels()

    @Inject
    lateinit var userAccountManager: UserAccountManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_welcome)

        userAccountManager.continueLoginFlow.observe(this,{
            if(it){
                startMainActivity()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null){
            startMainActivity()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                 val googleSignInAccount = task.getResult(ApiException::class.java)!!
                 viewModelUser.loginWithGoogle(googleSignInAccount).reObserve(this,{
                     when(it.status){
                         Status.SUCCESS ->{

                         }
                         Status.LOADING ->{

                         }
                         Status.ERROR ->{

                         }

                     }
                 })

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                // ...
                e.printStackTrace()
                Snackbar.make(findViewById(R.id.container_activity_welcome), "Authentication Failed for google.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }



    private fun startMainActivity() =
       Intent(this, MainActivity::class.java).also {
           startActivity(it)
           finish()
       }


    override fun loginWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_token))
                .requestEmail()
                .requestProfile()
                .build()
        val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent;
        startActivityForResult(signInIntent, requestCodeGoogleSignIn)
    }

}