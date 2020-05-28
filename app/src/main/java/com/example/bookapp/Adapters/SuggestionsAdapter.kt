package com.example.bookapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookapp.databinding.LayoutPostItemBinding
import com.example.bookapp.fragments.ExpandedPostFragmentDirections
import com.example.bookapp.models.LowDataPost
import java.util.*

class SuggestionsAdapter : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {
    private var context: Context? = null
    var data: ArrayList<LowDataPost> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutSuggestionItemBinding = LayoutPostItemBinding
                .inflate(layoutInflater, parent, false)
        return ViewHolder(layoutSuggestionItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPost = data[position]
        holder.bindData(currentPost)
        Glide.with(context!!)
                .load(currentPost.image)
                .into(holder.binding.suggestionImage)
        holder.binding.root.setOnClickListener {
            val action: NavDirections = ExpandedPostFragmentDirections.actionGlobalExpandedItemFragment(currentPost.id)
            Navigation.findNavController(holder.binding.root).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(var binding: LayoutPostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(post: LowDataPost) {
            binding.suggestionPost = post
        }

    }

}