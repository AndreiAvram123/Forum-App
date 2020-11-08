package com.andrei.kit.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.andrei.kit.R
import com.andrei.kit.databinding.LayoutItemPostBinding
import com.andrei.kit.fragments.ExpandedPostFragmentDirections
import com.andrei.kit.utils.getConnectivityManager
import com.andrei.kit.models.Post
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
                    val action: NavDirections = ExpandedPostFragmentDirections.actionGlobalExpandedItemFragment(posts[adapterPosition].id)
                    Navigation.findNavController(binding.root).navigate(action)

                }
            }
        }
    }
}