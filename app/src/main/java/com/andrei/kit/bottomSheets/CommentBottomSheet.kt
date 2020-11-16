package com.andrei.kit.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrei.kit.databinding.BottomSheetCommentBinding
import com.andrei.kit.models.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentBottomSheet(
        private val callback: (commentContent: String) -> Unit,
        private val user: User): BottomSheetDialogFragment() {

    private lateinit var binding:BottomSheetCommentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       binding = BottomSheetCommentBinding.inflate(inflater, container, false)
        binding.user = user
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
         binding.apply {
             commentContent.text.clear()
             progressBar.visibility = View.VISIBLE
             submitCommentButton.visibility = View.INVISIBLE
         }
    }


}