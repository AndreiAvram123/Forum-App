package com.andrei.kit.Adapters

import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andrei.kit.R
import com.andrei.kit.databinding.PostItemHomePageBinding
import com.andrei.kit.fragments.ExpandedPostFragmentDirections
import com.andrei.kit.models.Post
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import okhttp3.internal.notify

class HomeAdapter(
        private val removeFromFavorites : (post:Post)->Unit,
        private val addToFavorites: (post:Post)->Unit,
        private val connectivityManager: ConnectivityManager
) : PagedListAdapter<Post, HomeAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object :
                DiffUtil.ItemCallback<Post>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldPost: Post,
                                         newPost: Post) = oldPost.id == newPost.id
            override fun areContentsTheSame(oldPost: Post,
                                            newPost: Post) =  oldPost == newPost
        }
    }

    fun notifyPostChanged(post: Post){
        currentList?.let {
              notifyItemChanged(it.indexOf(post))
          }
    }

    inner class ViewHolder(val binding: PostItemHomePageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val post = getItem(position) ?: return
            binding.post = post
            binding.postTitleHomeItem.setOnClickListener {
                if (isInternetActive()) {
                    goToExpandedFragment(post)
                } else {
                     displayInternetConnectionError(binding)
                }
            }

            binding.bookmarkPostItem.setOnClickListener{
                if(isInternetActive()){
                    if(post.isFavorite){
                       removeFromFavorites(post)
                    }else {
                        addToFavorites(post)
                    }
                }else{
                    displayInternetConnectionError(binding)
                }
            }
            val images = post.images.split(", ")

            binding.postCarousel.apply {
                size= images.size
                indicatorAnimationType = IndicatorAnimationType.SLIDE
                setCarouselViewListener { view, position ->
                   val imageView = view.findViewById<ImageView>(R.id.image_item_carousel)
                    Glide.with(imageView).load(images[position])
                            .into(imageView)
                  view.setOnClickListener {
                      goToExpandedFragment(post)
                  }
                }
                show()
            }
        }

        private fun goToExpandedFragment(post: Post) {
            val action: NavDirections = ExpandedPostFragmentDirections.actionGlobalExpandedItemFragment(post.id)
            Navigation.findNavController(binding.root).navigate(action)
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