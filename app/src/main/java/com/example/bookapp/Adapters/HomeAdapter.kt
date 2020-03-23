package com.example.bookapp.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.databinding.LoadingItemListBinding
import com.example.bookapp.databinding.PostItemHomePageBinding
import com.example.bookapp.fragments.ExpandedItemFragmentDirections
import com.example.bookapp.models.Post

class HomeAdapter(val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class State {
        LOADING, LOADED
    }

    enum class ItemTypes(val id: Int) {
        LOADING_ITEM(1), LIST_ITEM(2)
    }

    private var posts: ArrayList<Post> = ArrayList()
    var currentState: State = State.LOADED
    val loadingObject: Post = Post.buildNullSafeObject()


    fun addData(newPosts: ArrayList<Post>) {
        val oldIndex: Int = posts.size
        posts.addAll(newPosts)
        notifyItemRangeInserted(oldIndex, posts.size)
        currentState = State.LOADED
        posts.remove(loadingObject)
    }

    override fun getItemViewType(position: Int): Int {
        Log.d("test", position.toString())
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
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == posts.size - 2) {
                    loadMore()
                }
            }

            private fun loadMore() {
                if (currentState == State.LOADED) {
                    currentState = State.LOADING
                    posts.add(loadingObject)
                    callback.requestMoreData()
                }
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

    fun setData(posts: ArrayList<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: PostItemHomePageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.post = post
            binding.root.setOnClickListener {
                val action: NavDirections = ExpandedItemFragmentDirections.actionGlobalExpandedItemFragment(post.postID)
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }

    private inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)


    interface Callback {
        fun requestMoreData()
    }

}