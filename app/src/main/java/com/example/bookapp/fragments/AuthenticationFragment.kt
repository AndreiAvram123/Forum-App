package com.example.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bookapp.databinding.FragmentAuthenticationBinding
import com.google.android.gms.common.SignInButton

class AuthenticationFragment : Fragment() {

    private lateinit var fragmentCallback: FragmentCallback
    private lateinit var binding: FragmentAuthenticationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentCallback = requireActivity() as FragmentCallback
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        initializeViews()
        attachListeners()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun attachListeners() {
        binding.signInWithEmail.setOnClickListener {
            val action = AuthenticationFragmentDirections.actionAuthenticationFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }


    private fun initializeViews() {
        binding.signInGoogle.setSize(SignInButton.SIZE_WIDE)
        binding.signInGoogle.setOnClickListener {
            fragmentCallback.loginWithGoogle()
        }
    }

    interface FragmentCallback {
        fun loginWithGoogle()
    }
}