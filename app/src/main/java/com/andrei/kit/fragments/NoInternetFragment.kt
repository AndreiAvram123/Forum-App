package com.andrei.kit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andrei.kit.databinding.FragmentNoInternetBinding
import com.andrei.kit.getConnectivityManager

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