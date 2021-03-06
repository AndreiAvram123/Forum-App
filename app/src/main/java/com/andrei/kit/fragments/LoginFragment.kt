package com.andrei.kit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.andrei.kit.R
import com.andrei.kit.databinding.LayoutLoginBinding
import com.andrei.kit.utils.reObserve
import com.andrei.kit.viewModels.ViewModelAuth
import com.google.android.gms.common.SignInButton

class LoginFragment : Fragment() {

    private lateinit var binding: LayoutLoginBinding
    private val viewModelAuth: ViewModelAuth by activityViewModels()
    private lateinit var fragmentCallback: FragmentCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LayoutLoginBinding.inflate(inflater, container, false)

        fragmentCallback = requireActivity() as FragmentCallback
        initializeUI()
        viewModelAuth.authenticationState.reObserve(viewLifecycleOwner,{
            if(!it.isNullOrEmpty()) {
                displayErrorMessage(it)
            }
        })

        return binding.root
    }


    private fun attemptLogin() {
        val email = binding.emailLogin.text.trim().toString()
        val password = binding.password.text.trim().toString()
        if (areLoginDetailsValid(email, password)) {
            viewModelAuth.login(email, password)
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