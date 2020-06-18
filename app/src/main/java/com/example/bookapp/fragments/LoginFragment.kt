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
import com.example.bookapp.databinding.FragmentLoginBinding
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.repositories.OperationStatus

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModelUser: ViewModelUser by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        initializeUI()
        viewModelUser.loginResult.observe(viewLifecycleOwner, Observer {
            if (it == OperationStatus.ONGOING) {
                showProgressIndicator()
            }
            if (it == OperationStatus.FAILED) {
                hideProgressIndicator()
                displayErrorMessage("Invalid credentials")
            }
        })
        return binding.root

    }


    private fun attemptLogin() {
        val username = binding.usernamelFieldLogin.text.trim().toString()
        val password = binding.passwordFieldLogin.text.trim().toString()
        if (areLoginDetailsValid(username, password)) {
            viewModelUser.login(username, password)
        }
    }


    private fun initializeUI() {
        configureButtons()
    }


    private fun configureButtons() {
        binding.signUpButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment();
            findNavController().navigate(action)

        }
        binding.signIn.setOnClickListener { attemptLogin() }
    }


    private fun showProgressIndicator() {
        binding.signIn.visibility = View.INVISIBLE
        binding.loggingProgressBar.visibility = View.VISIBLE
        binding.signUpButton.isClickable = false
    }

    private fun hideProgressIndicator() {
        binding.signIn.visibility = View.VISIBLE
        binding.loggingProgressBar.visibility = View.INVISIBLE
        binding.signUpButton.isClickable = true
    }



    private fun areLoginDetailsValid(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            displayErrorMessage(getString(R.string.fields_not_completed))
            return false
        }
        if (password.isEmpty()) {
            displayErrorMessage(getString(R.string.fields_not_completed))
            return false
        }
        return true
    }

    private fun displayErrorMessage(message: String) {
        binding.errorMessage.visibility = View.VISIBLE
        binding.errorMessage.text = message
    }


}