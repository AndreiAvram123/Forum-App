package com.example.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentSignUpBinding
import com.example.bookapp.isEmail

class SignUpFragment : AuthenticationFragmentTemplate() {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        initializeUI()

        return binding.root
    }

    public override fun configureButtons() {
        binding.finishSignUp.setOnClickListener { attemptSignUp() }
        binding.backImageSignUp.setOnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }

    override fun displayErrorMessage(message: String) {
        binding.errorMessage.visibility = View.VISIBLE
        binding.errorMessage.text = message
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
    override fun toggleLoadingBar() {
        val finishButton = binding.finishSignUp
        val progressBar = binding.loadingProgressBar
        if (finishButton.visibility == View.VISIBLE) {
            finishButton.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            finishButton.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
        clearFields()
    }


    private fun areCredentialsValid(email: String, password: String, reenteredPassword: String,
                                    nickname: String): Boolean {
        if (email.isEmail()) {
            displayErrorMessage(getString(R.string.error_invalid_email))
            return false
        }
        if (password.isEmpty()) {
            displayErrorMessage(getString(R.string.no_password))
            return false
        }
        if (password != reenteredPassword) {
            displayErrorMessage(getString(R.string.password_match))
            return false
        }
        if (nickname.isEmpty()) {
            displayErrorMessage(getString(R.string.error_no_nickname))
        }
        return true
    }

    /**
     * ACTION - SIGN UP
     */
    private fun attemptSignUp() {
        val email = binding.emailField.text.toString().trim { it <= ' ' }
        val password = binding.passwordField.text.toString().trim { it <= ' ' }
        val reenteredPassword = binding.reenterePasswordField.text.toString().trim { it <= ' ' }
        val nickname = binding.nicknameField.text.toString().trim { it <= ' ' }
        if (areCredentialsValid(email, password, reenteredPassword, nickname)) {
            toggleLoadingBar()
        }
    }

    public override fun clearFields() {
        binding.emailField.setText("")
        binding.passwordField.setText("")
        binding.reenterePasswordField.setText("")
        binding.nicknameField.setText("")
    }

    public override fun initializeUI() {
        customiseFields()
        configureButtons()
    }

    public override fun customiseFields() {
        //   customiseField(binding.emailField, binding.hintEmail)
        //customiseField(binding.passwordField, binding.hintPassword)
        //customiseField(binding.reenterePasswordField, binding.hintReenterPassword)
        //customiseField(binding.re, hintNickname)
    }

    interface SignUpFragmentCallback {
        fun signUp(email: String?, password: String?, nickname: String?)
    }

}