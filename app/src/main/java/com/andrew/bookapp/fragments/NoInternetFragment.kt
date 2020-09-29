package com.andrew.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andrew.bookapp.databinding.FragmentNoInternetBinding
import com.andrew.bookapp.getConnectivityManager

class NoInternetFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val binding = FragmentNoInternetBinding.inflate(inflater, container, false)
        binding.retry.setOnClickListener {
            if (requireContext().getConnectivityManager().activeNetwork != null) {
                findNavController().popBackStack()
            }
        }
        return binding.root
    }
}