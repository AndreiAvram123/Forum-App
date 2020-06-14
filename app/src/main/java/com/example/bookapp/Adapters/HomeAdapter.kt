package com.example.bookapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.AppUtilities
import com.example.bookapp.R
import com.example.bookapp.databinding.LoadingItemListBinding
import com.example.bookapp.databinding.PostItemHomePageBinding
import com.example.bookapp.fragments.ExpandedPostFragmentDirections
import com.example.bookapp.models.Post
import com.example.bookapp.viewModels.ViewModelPost
import com.google.android.material.snackbar.Snackbar
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList

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
                if (AppUtilities.isNetworkAvailable(binding.root.context)) {
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