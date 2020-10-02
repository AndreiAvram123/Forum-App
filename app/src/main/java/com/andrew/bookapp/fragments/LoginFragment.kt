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
import com.andrew.bookapp.databinding.LayoutLoginBinding
import com.andrew.bookapp.viewModels.ViewModelUser
import com.andrew.dataLayer.repositories.OperationStatus
import com.google.android.gms.common.SignInButton

class LoginFragment : Fragment() {

    private lateinit var binding: LayoutLoginBinding
    private val viewModelUser: ViewModelUser by activityViewModels()
    private lateinit var fragmentCallback: FragmentCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LayoutLoginBinding.inflate(inflater, container, false)

        fragmentCallback = requireActivity() as FragmentCallback
        initializeUI()
        return binding.root

    }


    private fun attemptLogin() {
        val username = binding.username.text.trim().toString()
        val password = binding.password.text.trim().toString()
        if (areLoginDetailsValid(username, password)) {
            viewModelUser.login(username, password).observe(viewLifecycleOwner, Observer {
                if (it == OperationStatus.FAILED) {

                    displayErrorMessage("Invalid credentials")
                }
            })
        }
    }


    private fun initializeUI() {
        binding.registerButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment();
            findNavController().navigate(action)

        }
        binding.loginButton.setOnClickListener { attemptLogin() }
        binding.signInGoogle.setSize(SignInButton.SIZE_WIDE)
        binding.signInGoogle.setOnClickListener {
            fragmentCallback.loginWithGoogle()
        }
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
        binding.errorMessageLogin.visibility = View.VISIBLE
        binding.errorMessageLogin.text = message
    }

    interface FragmentCallback {
        fun loginWithGoogle()
    }

}