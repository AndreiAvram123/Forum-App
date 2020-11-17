package com.andrei.kit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.andrei.kit.R
import com.andrei.kit.databinding.LayoutSignUpBinding
import com.andrei.kit.utils.isEmail
import com.andrei.kit.viewModels.ViewModelAuth
import com.andrei.kit.utils.reObserve

class RegisterFragment : Fragment() {

    private lateinit var binding: LayoutSignUpBinding
    private val viewModelAuth: ViewModelAuth by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LayoutSignUpBinding.inflate(inflater, container, false)
        initializeUI()
        viewModelAuth.registrationError.reObserve(viewLifecycleOwner,{
                    displayErrorMessage(it)
                   showButton()

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
        binding.finishSignUp.visibility = View.INVISIBLE
    }

    private fun showButton() {
        binding.finishSignUp.visibility = View.VISIBLE
    }


    private fun areCredentialsValid(email: String, password: String, reenteredPassword: String,
                                    nickname: String): Boolean {
        if (!email.isEmail()) {
            displayErrorMessage(getString(R.string.invalid_email))
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
            viewModelAuth.register(username = username,
                    email= email,
                    password= password)
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