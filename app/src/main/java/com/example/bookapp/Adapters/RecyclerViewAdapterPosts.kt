package com.example.bookapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.databinding.LayoutItemPostBinding
import com.example.bookapp.fragments.ExpandedItemFragmentDirections
import com.example.bookapp.models.Post
import java.util.*

class RecyclerViewAdapterPosts : RecyclerView.Adapter<RecyclerViewAdapterPosts.ViewHolder>() {
    private var posts = ArrayList<Post>()

    fun addData(data: List<Post>) {
        posts = ArrayList(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LayoutItemPostBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.layout_item_post, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.post = posts[position]
        holder.itemView.setOnClickListener {
            val action: NavDirections = ExpandedItemFragmentDirections.actionGlobalExpandedItemFragment(posts[position].postID)
            Navigation.findNavController(holder.binding.root).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class ViewHolder(val binding: LayoutItemPostBinding) : RecyclerView.ViewHolder(binding.root)
}