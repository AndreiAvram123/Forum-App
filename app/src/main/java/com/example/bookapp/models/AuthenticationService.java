package com.example.bookapp.models;

import android.app.Activity;
import android.content.Intent;

import com.example.bookapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class AuthenticationService {
    private static AuthenticationService instance;

    public static AuthenticationService getInstance(){
        if(instance == null){
            instance = new AuthenticationService();
        }
        return instance;
    }

    public Intent getGoogleSignInIntent(Activity activity){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(activity, gso);
        return googleSignInClient.getSignInIntent();
    }
}
