package com.socialMedia.bookapp.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.socialMedia.bookapp.databinding.FragmentImageZoomBinding
import com.socialMedia.bookapp.toDrawable


class ImageZoomFragment : Fragment() {

    private val args: ImageZoomFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentImageZoomBinding.inflate(inflater, container, false)

        if (args.localImage) {
            binding.photoView.setImageDrawable(Uri.parse(args.imageURl).toDrawable(requireContext()))
        } else {
            binding.imageURL = args.imageURl
        }


        binding.backButtonImage.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

}