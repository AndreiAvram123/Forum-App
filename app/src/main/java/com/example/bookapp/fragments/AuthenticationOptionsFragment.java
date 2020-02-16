package com.example.bookapp.fragments;


import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookapp.R;

public class AuthenticationOptionsFragment extends Fragment {


    private ConstraintLayout loginWithFacebookLayout;
    private ConstraintLayout loginWithGoogleLayout;
    private ConstraintLayout loginWithEmailLayout;
    private AuthenticationOptionsCallback authenticationOptionsCallback;

    public static AuthenticationOptionsFragment newInstance() {
        AuthenticationOptionsFragment authenticationOptionsFragment = new AuthenticationOptionsFragment();
        return authenticationOptionsFragment;
    }

    public AuthenticationOptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_fragment_authentication_options, container, false);
        authenticationOptionsCallback = (AuthenticationOptionsCallback) getActivity();
        initializeViews(layout);
        attachListeners();
        // Inflate the layout for this fragment
        return layout;
    }

    private void attachListeners() {
        loginWithFacebookLayout.setOnClickListener((layout)->{
        });
        loginWithGoogleLayout.setOnClickListener((layout)->{
            authenticationOptionsCallback.loginWithGoogle();

        });
        loginWithEmailLayout.setOnClickListener((layout)->{

        });
    }

    private void initializeViews(View layout) {
        loginWithFacebookLayout = layout.findViewById(R.id.login_with_facebook_item);
        loginWithGoogleLayout = layout.findViewById(R.id.login_with_google_item);
        loginWithEmailLayout = layout.findViewById(R.id.login_with_email_item);
    }


    public interface AuthenticationOptionsCallback {

        void loginWithFacebook();

        void loginWithGoogle();

        void showLoginWithEmailFragment();
    }
}
