package com.example.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.bookapp.databinding.FragmentLoginBinding
import com.example.bookapp.isEmail

class LoginFragment : AuthenticationFragmentTemplate() {

    private lateinit var binding: FragmentLoginBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        initializeUI()
        return binding.root
    }


    private fun attemptLogin() {
        val email = binding.emailFieldLogin.text.trim().toString()
        val password = binding.passwordFieldLogin.text.trim().toString()
        if (areLoginDetailsValid(email, password)) {
            //todo
            //login
            toggleLoadingBar()
        }
    }

    override fun clearFields() {
        binding.emailFieldLogin.text.clear()
        binding.passwordFieldLogin.text.clear()
    }

    public override fun initializeUI() {
        customiseFields()
        configureButtons()
    }

    public override fun customiseFields() {
        customiseField(binding.emailFieldLogin, binding.emailFieldLogin)
        customiseField(binding.passwordFieldLogin, binding.passwordHintLogin)
    }

    public override fun configureButtons() {
        binding.signUpButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment();
            findNavController().navigate(action)

        }
        binding.signIn.setOnClickListener { attemptLogin() }
    }


    override fun toggleLoadingBar() {
        val signInButton = binding.signIn
        val signUpButton = binding.signUpButton
        val loadingBar = binding.loggingProgressBar

        if (signInButton.visibility == View.VISIBLE) {
            signInButton.visibility = View.INVISIBLE
            loadingBar.visibility = View.VISIBLE
            signUpButton.isClickable = false
        } else {
            signUpButton.isClickable = true
            signInButton.visibility = View.VISIBLE
            loadingBar.visibility = View.INVISIBLE
        }
    }

    /**
     * This method is used to check if the pushLoginViaEmailRequest
     * details are valid or not.We need to check the following:
     *
     *
     * If the email is valid using the method from the Utilities class
     * (the email should have the following format [a-zA-Z0-9]+@[a-z]+\.[a-z]+)
     *
     *
     * If the password field is not empty and the length of the password is AT LEST 6
     * characters( Firebase does not allow password that have less that 6 characters)
     *
     * @return
     */
    private fun areLoginDetailsValid(email: String, password: String): Boolean {
        if (email.isEmail()) {
            displayErrorMessage("Please enter a valid email")
            return false
        }
        if (password.isEmpty() || password.length < 6) {
            displayErrorMessage("Please enter a password")
            return false
        }
        return true
    }

    override fun displayErrorMessage(message: String) {
        binding.errorMessage.visibility = View.VISIBLE
        binding.errorMessage.text = message
    }


}