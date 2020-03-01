package com.example.bookapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookapp.R;
import com.example.bookapp.api.ApiManager;
import com.example.bookapp.fragments.AuthenticationOptionsFragment;
import com.example.bookapp.fragments.LoginFragment;
import com.example.bookapp.fragments.SignUpFragment;
import com.example.bookapp.models.ApiConstants;
import com.example.bookapp.models.AuthenticationService;
import com.example.bookapp.models.User;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class WelcomeActivity extends AppCompatActivity implements LoginFragment.LoginFragmentCallback, SignUpFragment.SignUpFragmentCallback,
        AuthenticationOptionsFragment.AuthenticationOptionsCallback, ApiManager.ApiManagerAuthenticationCallback {

    private static final int request_code_google_sign_in = 1;
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    private CallbackManager mCallbackManager = CallbackManager.Factory.create();
    private GoogleSignInAccount googleSignInAccount = null;

    private ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_welcome);
        displayAuthenticationOptionsFragment();
        initializeApiManager();
        setFlatWelcomeActivityShown();
    }

    private void setFlatWelcomeActivityShown() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.welcome_activity_shown_key), true);
        editor.apply();
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
                googleSignInAccount = task.getResult(ApiException.class);
                apiManager.authenticateWithThirdPartyAccount(googleSignInAccount.getEmail());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Snackbar.make(findViewById(R.id.container_activity_welcome), "Authentication Failed for google.", Snackbar.LENGTH_SHORT).show();

            }
        }
    }

    private void initializeApiManager() {
        apiManager = ApiManager.getInstance(this);
        apiManager.setApiManagerAuthenticationCallback(this);
    }


    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void pushLoginViaEmailRequest(String username, String password) {

    }

    @Override
    public void signUp(String email, String password, String nickname) {

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

    @Override
    public void onAuthenticationResponse(String response) {
        Log.d(TAG, response);
        int responseCode;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            responseCode = jsonObject.getInt("responseCode");
            if (responseCode == ApiConstants.RESPONSE_CODE_ACCOUNT_EXISTS) {
                saveUserUserInMemory(jsonObject.getString("userID"), jsonObject.getString("username"));
            }
            if (responseCode == ApiConstants.RESPONSE_CODE_ACCOUNT_UNEXISTENT) {
                User.Builder builder = new User.Builder();
                if (googleSignInAccount != null) {
                    buildUserFromGoogleAccount(builder);
                    apiManager.createThirdPartyUserAccount(builder.create());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onThirdPartyAccountCreated(JSONObject responseJson) {
        try {
            if (responseJson.getInt("responseCode") == ApiConstants.RESPONSE_CODE_ACCOUNT_CREATED) {
                Log.d(TAG, responseJson.toString());
                saveUserUserInMemory(responseJson.getString("userID"), responseJson.getString("username"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void buildUserFromGoogleAccount(User.Builder builder) {
        builder.setUserID(googleSignInAccount.getId());
        builder.setUsername(googleSignInAccount.getDisplayName());
        builder.setEmail(googleSignInAccount.getEmail());
        builder.setImageURL(googleSignInAccount.getPhotoUrl().toString());
    }

    private void saveUserUserInMemory(String userID, String username) {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit();
        editor.putString(getString(R.string.key_username), username);
        editor.putString(getString(R.string.key_user_id), userID);
        editor.apply();
        startMainActivity();
    }
}
