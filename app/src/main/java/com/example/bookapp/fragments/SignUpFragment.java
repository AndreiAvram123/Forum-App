package com.example.bookapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookapp.R;
import com.example.bookapp.AppUtilities;

public class SignUpFragment extends AuthenticationFragment {

    private EditText emailField;
    private EditText passwordField;
    private EditText reenteredPasswordField;
    private EditText nicknameField;
    private Button finishButton;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private TextView hintEmail;
    private TextView hintPassword;
    private TextView hintReenteredPassword;
    private TextView hintNickname;
    private ImageView backImage;
    private SignUpFragmentCallback signUpFragmentCallback;


    static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_sign_up, container, false);

        signUpFragmentCallback = (SignUpFragmentCallback) getActivity();


        initializeUI(layout);

        return layout;

    }


    @Override
    void configureButtons() {
        finishButton.setOnClickListener(view -> attemptAction());
        backImage.setOnClickListener(image -> getActivity().getSupportFragmentManager().popBackStack());
    }

    @Override
    public void displayErrorMessage(String message) {
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(message);
    }

    /**
     * Once the user has pressed the finishButton and
     * the credentials are valid it may take some time
     * until Firebase processes our SignUp request(usually this does not happen)
     * We hide the finishButton  and show the loadingBar
     * until Firebase has processed the request
     * !!!!!!!!!
     * THIS METHOD IS ALSO CALLED FROM @activity StartScreenActivity
     * HAS BEEN PROCESSED
     */
    @Override
    public void toggleLoadingBar() {
        if (finishButton.getVisibility() == View.VISIBLE) {
            finishButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            finishButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
        clearFields();
    }


    private void initializeViews(View layout) {
        hintEmail = layout.findViewById(R.id.hint_enter_email);
        hintPassword = layout.findViewById(R.id.hint_enter_password);
        hintReenteredPassword = layout.findViewById(R.id.hint_reenter_password);
        hintNickname = layout.findViewById(R.id.hint_enter_nickname);

        emailField = layout.findViewById(R.id.email_field_sign_up);
        passwordField = layout.findViewById(R.id.password_field_sign_up);
        reenteredPasswordField = layout.findViewById(R.id.reentered_password_field);
        nicknameField = layout.findViewById(R.id.nickname_field);

        finishButton = layout.findViewById(R.id.finish_sign_up);
        progressBar = layout.findViewById(R.id.loading_progress_bar_sign_up);
        errorTextView = layout.findViewById(R.id.error_message_sign_up);
        backImage = layout.findViewById(R.id.back_image_sign_up);

        configureButtons();

    }


    private boolean areCredentialsValid(String email, String password, String reenteredPassword,
                                        String nickname) {

        if (!AppUtilities.isEmailValid(email)) {
            displayErrorMessage(getString(R.string.error_invalid_email));
            return false;
        }
        if (password.isEmpty()) {
            displayErrorMessage(getString(R.string.no_password));
            return false;
        }
        if (!password.equals(reenteredPassword)) {
            displayErrorMessage(getString(R.string.password_match));
            return false;
        }
        if (nickname.isEmpty()) {
            displayErrorMessage(getString(R.string.error_no_nickname));
        }
        return true;

    }


    /**
     * ACTION - SIGN UP
     */
    @Override
    void attemptAction() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String reenteredPassword = reenteredPasswordField.getText().toString().trim();
        String nickname = nicknameField.getText().toString().trim();

        if (areCredentialsValid(email, password, reenteredPassword, nickname)) {
            toggleLoadingBar();
            signUpFragmentCallback.signUp(email, password, nickname);

        }
    }

    @Override
    void clearFields() {
        emailField.setText("");
        passwordField.setText("");
        reenteredPasswordField.setText("");
        nicknameField.setText("");
    }

    @Override
    void initializeUI(View layout) {
        initializeViews(layout);
        customiseFields();
        configureButtons();
    }

    @Override
    void customiseFields() {
        customiseField(emailField, hintEmail);
        customiseField(passwordField, hintPassword);
        customiseField(reenteredPasswordField, hintReenteredPassword);
        customiseField(nicknameField, hintNickname);
    }

    public interface SignUpFragmentCallback {
        void signUp(String email, String password, String nickname);
    }

}
