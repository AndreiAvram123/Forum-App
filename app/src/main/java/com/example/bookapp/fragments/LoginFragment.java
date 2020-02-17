package com.example.bookapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookapp.AppUtilities;
import com.example.bookapp.R;

public class LoginFragment extends AuthenticationFragment {

    private TextView emailHint;
    private TextView passwordHint;
    private TextView errorMessage;
    private EditText emailField;
    private EditText passwordField;
    private ProgressBar loadingBar;
    private Button signInButton;
    private Button signUpButton;
    private LoginFragmentCallback loginFragmentCallback;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_login, container, false);
        loginFragmentCallback = (LoginFragmentCallback) getActivity();
        initializeUI(layout);
        return layout;
    }


    /**
     * ACTION - LOGIN
     * This method gets the text from the emailField and the
     * passwordField and then calls the method areLoginDetailsValid()
     * if the method called return true then call loginFragmentCallback.loginUser()
     */

    @Override
    void attemptAction() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if (areLoginDetailsValid(email, password)) {
            toggleLoadingBar();
            loginFragmentCallback.pushLoginViaEmailRequest(email, password);
        }
    }

    @Override
    void clearFields() {
        emailField.setText("");
        passwordField.setText("");
    }

    @Override
    void initializeUI(View layout) {
        initializeViews(layout);
        customiseFields();
        configureButtons();

    }

    @Override
    void customiseFields() {
        customiseField(emailField, emailHint);
        customiseField(passwordField, passwordHint);
    }

    @Override
    void configureButtons() {
        signUpButton.setOnClickListener(view -> getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_activity_welcome,SignUpFragment.newInstance())
                .addToBackStack(null)
                .commit());
        signInButton.setOnClickListener(view -> attemptAction());
    }


    private void initializeViews(View layout) {
        emailHint = layout.findViewById(R.id.email_hint_login);
        passwordHint = layout.findViewById(R.id.password_hint_login);
        emailField = layout.findViewById(R.id.email_edit_text_main);
        passwordField = layout.findViewById(R.id.password_edit_text_main);
        errorMessage = layout.findViewById(R.id.error_message_text_view);
        loadingBar = layout.findViewById(R.id.logging_progress_bar);
        signInButton = layout.findViewById(R.id.sign_in_button);
        signUpButton = layout.findViewById(R.id.sign_up_button);
    }


    /**
     * Once the user has pressed the signInButton and
     * the credentials are valid it may take some time
     * until Firebase processes our pushLoginViaEmailRequest request(usually this does not happen)
     * We hide the signInButton and the signUpButton and show the loadingBar
     * until Firebase has processed the request
     * THIS METHOD IS ALSO CALLED FROM @activity StartScreenActivity
     */
    public void toggleLoadingBar() {

        if (signInButton.getVisibility() == View.VISIBLE) {
            signInButton.setVisibility(View.INVISIBLE);
            loadingBar.setVisibility(View.VISIBLE);
            signUpButton.setClickable(false);

        } else {
            signUpButton.setClickable(true);
            signInButton.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.INVISIBLE);
        }

    }


    /**
     * This method is used to check if the pushLoginViaEmailRequest
     * details are valid or not.We need to check the following:
     * <p>
     * If the email is valid using the method from the Utilities class
     * (the email should have the following format [a-zA-Z0-9]+@[a-z]+\.[a-z]+)
     * <p>
     * If the password field is not empty and the length of the password is AT LEST 6
     * characters( Firebase does not allow password that have less that 6 characters)
     *
     * @return
     */
    private boolean areLoginDetailsValid(String email, String password) {
        if (!AppUtilities.isEmailValid(email)) {
            displayErrorMessage("Please enter a valid email");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            displayErrorMessage("Please enter a password");
            return false;
        }
        return true;

    }

    @Override
    public void displayErrorMessage(String message) {
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }

    public interface LoginFragmentCallback {
        void pushLoginViaEmailRequest(String email, String password);

    }


}
