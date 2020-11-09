package com.andrei.kit.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrei.kit.databinding.BottomSheetCommentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentBottomSheet(val callback: (commentContent: String) -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding:BottomSheetCommentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       binding = BottomSheetCommentBinding.inflate(inflater, container, false)
        attachListener(binding)
        return binding.root
    }

    private fun attachListener(binding: BottomSheetCommentBinding) {
        binding.submitCommentButton.setOnClickListener {
            val commentContent = binding.commentContent.text.toString()
            if (commentContent.trim().isNotEmpty()) {
                callback(commentContent)
            }
        }
    }
     fun showLoading(){
        binding.commentContent.text.clear()
        binding.progressBar.visibility = View.VISIBLE
        binding.submitCommentButton.visibility = View.INVISIBLE
    }


}