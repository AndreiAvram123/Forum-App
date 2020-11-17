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
import com.andrei.kit.utils.getTrimmedText
import com.andrei.kit.utils.isEmail
import com.andrei.kit.viewModels.ViewModelAuth
import com.andrei.kit.utils.reObserve

class RegisterFragment : Fragment() {

    private lateinit var binding: LayoutSignUpBinding
    private val viewModelAuth: ViewModelAuth by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LayoutSignUpBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModelAuth = viewModelAuth
        initializeUI()

        return binding.root
    }


    private fun configureButtons() {
        binding.finishSignUp.setOnClickListener { getCredentials() }
        binding.backButton.setOnClickListener {
            val action = RegisterFragmentDirections.actionSignUpFragmentToLoginFragment2()
            findNavController().navigate(action)
        }
    }

    private fun hideButton(){
        binding.finishSignUp.visibility = View.INVISIBLE
    }


    private fun areCredentialsValid(email: String, password: String, reenteredPassword: String,
                                    nickname: String): Boolean {
        if (!email.isEmail()) {
            viewModelAuth.registrationError.value = getString(R.string.invalid_email)
            return false
        }
        if (password.isEmpty()) {
            viewModelAuth.registrationError.value  = getString(R.string.no_password)
            return false
        }
        if (password != reenteredPassword) {
            viewModelAuth.registrationError.value = getString(R.string.password_match)
            return false
        }
        if (nickname.isEmpty()) {
            viewModelAuth.registrationError.value = getString(R.string.error_no_nickname)
        }
        return true
    }

    private fun getCredentials() {
        val email = binding.email.getTrimmedText()
        val password = binding.password.getTrimmedText()
        val reenteredPassword = binding.reenterPassword.getTrimmedText()
        val username = binding.username.getTrimmedText()

        if (areCredentialsValid(email, password, reenteredPassword, username)) {
            viewModelAuth.register(username = username,
                    email= email,
                    password= password)
            hideButton()
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