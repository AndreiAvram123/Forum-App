package com.andrei.kit.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andrei.kit.R
import com.andrei.kit.databinding.PostItemHomePageBinding
import com.andrei.kit.fragments.ExpandedPostFragmentDirections
import com.andrei.kit.getConnectivityManager
import com.andrei.kit.models.Post
import com.google.android.material.snackbar.Snackbar

class HomeAdapter : PagedListAdapter<Post, HomeAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object :
                DiffUtil.ItemCallback<Post>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldConcert: Post,
                                         newConcert: Post) = oldConcert.id == newConcert.id

            override fun areContentsTheSame(oldConcert: Post,
                                            newConcert: Post) = oldConcert == newConcert
        }
    }

    inner class ViewHolder(val binding: PostItemHomePageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.post = post
            binding.root.setOnClickListener {
                if (binding.root.context.getConnectivityManager().activeNetwork != null) {
                    val action: NavDirections = ExpandedPostFragmentDirections.actionGlobalExpandedItemFragment(post.id)
                    Navigation.findNavController(binding.root).navigate(action)
                } else {
                    Snackbar.make(binding.root, binding.root.context.getString(R.string.no_internet_connection), Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostItemHomePageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        // Note that "concert" is a placeholder if it's null
        post?.let {
            holder.bind(post)
        }
    }

}