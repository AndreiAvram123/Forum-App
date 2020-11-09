package com.andrei.kit.Adapters

import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andrei.dataLayer.engineUtils.Status
import com.andrei.kit.R
import com.andrei.kit.databinding.PostItemHomePageBinding
import com.andrei.kit.fragments.ExpandedPostFragmentDirections
import com.andrei.kit.models.Post
import com.andrei.kit.utils.observeRequest
import com.andrei.kit.viewModels.ViewModelPost
import com.google.android.material.snackbar.Snackbar

class HomeAdapter(
        private val viewModelPost: ViewModelPost,
        private val connectivityManager: ConnectivityManager,
        private val viewLifecycleOwner: LifecycleOwner
) : PagedListAdapter<Post, HomeAdapter.ViewHolder>(DIFF_CALLBACK) {

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

        fun bind(position: Int) {
            val post = getItem(position) ?: return
            binding.post = post
            binding.postItemHomeImage.setOnClickListener {
                if (isInternetActive()) {
                    val action: NavDirections = ExpandedPostFragmentDirections.actionGlobalExpandedItemFragment(post.id)
                    Navigation.findNavController(binding.root).navigate(action)
                } else {
                     displayInternetConnectionError(binding)
                }
            }
            binding.bookmarkPostItem.setOnClickListener{
                if(isInternetActive()){
                    if(post.isFavorite){
                        viewModelPost.deletePostFromFavorites(post).observeRequest(viewLifecycleOwner,{
                            when(it.status){
                                Status.SUCCESS->{
                                    binding.post = post
                                    Snackbar.make(binding.root, binding.root.context.getString(R.string.removed_from_favorites), Snackbar.LENGTH_SHORT)
                                            .show()
                                    binding.notifyChange()
                                }
                            }
                        })
                    }else{
                        viewModelPost.addPostToFavorites(post).observeRequest(viewLifecycleOwner,{
                            when(it.status){
                                Status.SUCCESS ->{
                                    binding.post = post
                                    Snackbar.make(binding.root, binding.root.context.getString(R.string.added_to_favorites), Snackbar.LENGTH_SHORT)
                                            .show()
                                    binding.notifyChange()
                                }

                            }
                        })
                    }
                }else{
                    displayInternetConnectionError(binding)
                }
            }
        }
    }
    private fun isInternetActive():Boolean{
        return connectivityManager.activeNetwork !=null
    }
    private fun displayInternetConnectionError(binding: PostItemHomePageBinding){
        Snackbar.make(binding.root, binding.root.context.getString(R.string.no_internet_connection), Snackbar.LENGTH_SHORT)
                .show();
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostItemHomePageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(position)

    }

}