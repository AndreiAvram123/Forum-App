package com.example.bookapp.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.bookapp.R;
import com.google.android.gms.common.SignInButton;

public class AuthenticationFragment extends Fragment {


    private ConstraintLayout loginWithEmailLayout;
    private Callback callback;


    public AuthenticationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_fragment_authentication, container, false);
        callback = (Callback) getActivity();
        initializeViews(layout);
        attachListeners();
        // Inflate the layout for this fragment
        return layout;
    }

    private void attachListeners() {

        loginWithEmailLayout.setOnClickListener((layout) -> callback.showLoginWithEmailFragment());

    }

    private void initializeViews(View layout) {
        SignInButton signInButton = layout.findViewById(R.id.sign_in_google);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(button->callback.loginWithGoogle());

        loginWithEmailLayout = layout.findViewById(R.id.login_with_email_item);
    }


    public interface Callback {
        void loginWithGoogle();
        void showLoginWithEmailFragment();
    }
}
