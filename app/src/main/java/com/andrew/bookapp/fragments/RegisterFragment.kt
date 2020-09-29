package com.andrew.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.andrew.bookapp.R
import com.andrew.bookapp.databinding.LayoutSignUpBinding
import com.andrew.bookapp.isEmail
import com.andrew.bookapp.viewModels.ViewModelUser
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {

    private lateinit var binding: LayoutSignUpBinding
    private val viewModelUser: ViewModelUser by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LayoutSignUpBinding.inflate(inflater, container, false)
        initializeUI()
        viewModelUser.registrationMessage.observe(viewLifecycleOwner, Observer {
            hideButton()
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
        binding.backButton.setOnClickListener {
            val action = RegisterFragmentDirections.actionSignUpFragmentToLoginFragment2()
            findNavController().navigate(action)
        }
    }

    private fun displayErrorMessage(message: String) {
        binding.errorMessage.visibility = View.VISIBLE
        binding.errorMessage.text = message
    }


    private fun hideButton() {
        binding.finishSignUp.visibility = View.VISIBLE
    }

    private fun showButton() {
        binding.finishSignUp.visibility = View.INVISIBLE
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
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val reenteredPassword = binding.reenterPassword.text.toString().trim()
        val username = binding.username.text.toString().trim()

        if (areCredentialsValid(email, password, reenteredPassword, username)) {
            viewModelUser.register(username, email, password)
            showButton()
            clearFields()
        }
    }

    private fun clearFields() {
        binding.email.text.clear()
        binding.password.text.clear()
        binding.reenterPassword.text.clear()
        binding.username.text.clear()
    }

    private fun initializeUI() {

        configureButtons()
    }


}