package com.example.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentSignUpBinding
import com.example.bookapp.isEmail
import com.example.bookapp.viewModels.ViewModelUser
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModelUser: ViewModelUser by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        initializeUI()
        viewModelUser.registrationMessage.observe(viewLifecycleOwner, Observer {
            hideProgressBar()
            if (it == "Success") {
                Snackbar.make(binding.root, getString(R.string.account_created), Snackbar.LENGTH_LONG).show()
                hideErrors()
            } else {
                displayErrorMessage(it)
            }
        })
        return binding.root
    }

    private fun hideErrors() {
        binding.errorMessage.visibility = View.INVISIBLE
    }

    private fun configureButtons() {
        binding.finishSignUp.setOnClickListener { getCredentials() }
        binding.backImageSignUp.setOnClickListener {
            val action = RegisterFragmentDirections.actionSignUpFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }

    private fun displayErrorMessage(message: String) {
        binding.errorMessage.visibility = View.VISIBLE
        binding.errorMessage.text = message
    }


    private fun hideProgressBar() {
        binding.finishSignUp.visibility = View.VISIBLE
        binding.loadingProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressbar() {
        binding.finishSignUp.visibility = View.INVISIBLE
        binding.loadingProgressBar.visibility = View.VISIBLE
    }


    private fun areCredentialsValid(email: String, password: String, reenteredPassword: String,
                                    nickname: String): Boolean {
        if (!email.isEmail()) {
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

    private fun getCredentials() {
        val email = binding.emailField.text.toString().trim()
        val password = binding.passwordField.text.toString().trim()
        val reenteredPassword = binding.reenterePasswordField.text.toString().trim()
        val nickname = binding.nicknameField.text.toString().trim()

        if (areCredentialsValid(email, password, reenteredPassword, nickname)) {
            viewModelUser.register(nickname, email, password)
            showProgressbar()
            clearFields()
        }
    }

    private fun clearFields() {
        binding.emailField.text.clear()
        binding.passwordField.text.clear()
        binding.reenterePasswordField.text.clear()
        binding.nicknameField.text.clear()
    }

    private fun initializeUI() {

        configureButtons()
    }

    public fun customiseFields() {
        //   customiseField(binding.emailField, binding.hintEmail)
        //customiseField(binding.passwordField, binding.hintPassword)
        //customiseField(binding.reenterePasswordField, binding.hintReenterPassword)
        //customiseField(binding.re, hintNickname)
    }

    interface SignUpFragmentCallback {
        fun signUp(email: String?, password: String?, nickname: String?)
    }

}