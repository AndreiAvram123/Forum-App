package com.example.bookapp.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookapp.databinding.BottomSheetCommentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentBottomSheet(val callback: (commentContent: String) -> Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = BottomSheetCommentBinding.inflate(inflater, container, false)
        attachListener(binding)
        return binding.root
    }

    private fun attachListener(binding: BottomSheetCommentBinding) {
        binding.submitCommentButton.setOnClickListener {
            val commentContent = binding.commentContent.text.toString()
            if (commentContent.trim().isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                binding.submitCommentButton.visibility = View.INVISIBLE
                callback(commentContent)
                binding.commentContent.text.clear()
            }
        }
    }

}