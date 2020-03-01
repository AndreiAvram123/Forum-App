package com.example.bookapp.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.bookapp.R;

public class AuthenticationOptionsFragment extends Fragment {


    private ConstraintLayout loginWithGoogleLayout;
    private ConstraintLayout loginWithEmailLayout;
    private AuthenticationOptionsCallback authenticationOptionsCallback;
    private TextView loginAnonymouslyText;

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

        loginWithGoogleLayout.setOnClickListener((layout) -> authenticationOptionsCallback.loginWithGoogle());
        loginWithEmailLayout.setOnClickListener((layout) -> authenticationOptionsCallback.showLoginWithEmailFragment());
        // loginAnonymouslyText.setOnClickListener((layout)->authenticationOptionsCallback.loginAnonymously());

    }

    private void initializeViews(View layout) {
        loginWithGoogleLayout = layout.findViewById(R.id.login_with_google_item);
        loginWithEmailLayout = layout.findViewById(R.id.login_with_email_item);
        loginAnonymouslyText = layout.findViewById(R.id.sign_in_anonymously_text);
    }


    public interface AuthenticationOptionsCallback {
        void loginWithGoogle();
        void showLoginWithEmailFragment();
        void loginAnonymously();
    }
}