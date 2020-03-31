package com.example.bookapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.AppUtilities
import com.example.bookapp.R
import com.example.bookapp.databinding.LoadingItemListBinding
import com.example.bookapp.databinding.PostItemHomePageBinding
import com.example.bookapp.fragments.ExpandedItemFragmentDirections
import com.example.bookapp.models.Post
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList

class HomeAdapter(val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var timeout: Timer = Timer();

    enum class State {
        LOADING, LOADED
    }

    enum class ItemTypes(val id: Int) {
        LOADING_ITEM(1), LIST_ITEM(2)
    }

    private var posts: ArrayList<Post> = ArrayList()
    var currentState: State = State.LOADING
    private val loadingObject: Post = Post.buildNullSafeObject()


    fun addData(newPosts: ArrayList<Post>) {
        val oldIndex: Int = posts.size
        newPosts.forEach {
            if(!posts.contains(it)){
                posts.add(it)
            }
        }
        if(posts.size > oldIndex){
            notifyItemRangeInserted(oldIndex, posts.size)
        }
        toggleLoading()
        timeout.cancel()
        timeout = Timer()
    }

    private fun toggleLoading() {
        if (currentState == State.LOADING) {
            currentState = State.LOADED
            posts.remove(loadingObject)
            notifyItemRemoved(posts.size);
        } else {
            currentState = State.LOADING;
            posts.add(loadingObject)
            notifyItemInserted(posts.size-1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (posts[position] == loadingObject) {
            ItemTypes.LOADING_ITEM.id
        } else {
            ItemTypes.LIST_ITEM.id
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() > posts.size - 2 && currentState == State.LOADED) {
                    loadMore(recyclerView)
                }
            }

            private fun loadMore(recyclerView: RecyclerView) {
                toggleLoading()
                callback.requestMoreData()
                timeout.schedule(object : TimerTask() {
                    override fun run() {
                        recyclerView.post { toggleLoading() }
                    }
                }, 7000)
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemTypes.LIST_ITEM.id -> {
                val binding: PostItemHomePageBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.post_item_home_page, parent, false)
                ViewHolder(binding)
            }
            else -> {
                val binding: LoadingItemListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.loading_item_list, parent, false)
                LoadingViewHolder(binding.root)

            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(posts[position])
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class ViewHolder(val binding: PostItemHomePageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.post = post
            binding.root.setOnClickListener {
                if (AppUtilities.isNetworkAvailable(binding.root.context)) {
                    val action: NavDirections = ExpandedItemFragmentDirections.actionGlobalExpandedItemFragment(post.postID)
                    Navigation.findNavController(binding.root).navigate(action)
                } else {
                    Snackbar.make(binding.root, binding.root.context.getString(R.string.no_internet_connection), Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    private inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)


    interface Callback {
        fun requestMoreData()
    }
}