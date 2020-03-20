package com.example.bookapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.databinding.CommentItemListBinding
import com.example.bookapp.models.Comment

class AdapterComments(var comments: ArrayList<Comment>) : RecyclerView.Adapter<AdapterComments.ViewHolder>() {

    inner class ViewHolder(var binding: CommentItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindDataToView(comment: Comment) {
            binding.comment = comment;
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindDataToView(comments[position]);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context);
        val commentItemListBinding: CommentItemListBinding = CommentItemListBinding.inflate(layoutInflater, parent, false);
        return ViewHolder(commentItemListBinding);
    }

    override fun getItemCount(): Int {
        return comments.size;
    }
}