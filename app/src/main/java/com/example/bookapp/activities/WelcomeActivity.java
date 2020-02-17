package com.example.bookapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bookapp.MainActivity;
import com.example.bookapp.R;
import com.example.bookapp.fragments.AuthenticationOptionsFragment;
import com.example.bookapp.fragments.LoginFragment;
import com.example.bookapp.fragments.SignUpFragment;
import com.example.bookapp.models.AuthenticationService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity implements LoginFragment.LoginFragmentCallback, SignUpFragment.SignUpFragmentCallback,
        AuthenticationOptionsFragment.AuthenticationOptionsCallback {

    private static final int request_code_google_sign_in = 1;
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private CallbackManager mCallbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_welcome);
        displayAuthenticationOptionsFragment();
    }

    private void displayAuthenticationOptionsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_activity_welcome, AuthenticationOptionsFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    private void displayLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_activity_welcome, LoginFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

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
                Snackbar.make(findViewById(R.id.container_activity_welcome), "Authentication Failed for google.", Snackbar.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        startMainActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Snackbar.make(findViewById(R.id.container_main_activity), "Authentication Failed for firebase.", Snackbar.LENGTH_SHORT).show();
                    }

                    // ...
                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
     //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startMainActivity();
        }
    }

    @Override
    public void pushLoginViaEmailRequest(String username, String password) {

    }

    @Override
    public void signUp(String email, String password, String nickname) {

    }


    @Override
    public void prepareLoginWithFacebook(LoginButton loginButton) {
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        startMainActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(WelcomeActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                    // ...
                });
    }


    @Override
    public void loginWithGoogle() {
        startActivityForResult(AuthenticationService.getInstance().getGoogleSignInIntent(this), request_code_google_sign_in);
    }

    @Override
    public void showLoginWithEmailFragment() {
        displayLoginFragment();
    }

    @Override
    public void loginAnonymously() {
        startMainActivity();
    }
}
