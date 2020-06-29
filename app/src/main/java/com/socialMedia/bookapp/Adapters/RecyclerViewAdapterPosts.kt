package com.socialMedia.bookapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.socialMedia.bookapp.R
import com.socialMedia.bookapp.databinding.LayoutItemPostBinding
import com.socialMedia.bookapp.fragments.ExpandedPostFragmentDirections
import com.socialMedia.bookapp.getConnectivityManager
import com.socialMedia.bookapp.models.Post
import java.util.*

class RecyclerViewAdapterPosts : RecyclerView.Adapter<RecyclerViewAdapterPosts.ViewHolder>() {
    private val posts = ArrayList<Post>()

    fun setData(data: List<Post>) {
        posts.clear()
        posts.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LayoutItemPostBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.layout_item_post, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])

    }

    override fun getItemCount() = posts.size

    inner class ViewHolder(val binding: LayoutItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.post = post
            binding.root.setOnClickListener {
                if (binding.root.context.getConnectivityManager().activeNetwork != null) {
                    val action: NavDirections = ExpandedPostFragmentDirections.actionGlobalExpandedItemFragment(posts[position].id)
                    Navigation.findNavController(binding.root).navigate(action)

                }
            }
        }
    }
}