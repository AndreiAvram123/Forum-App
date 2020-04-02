package com.example.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.Adapters.RecyclerViewAdapterPosts
import com.example.bookapp.R
import com.example.bookapp.databinding.LayoutFragmentPostsDataBinding
import com.example.bookapp.models.Post
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
class FavoritePostsFragment : Fragment() {
    private var recyclerViewAdapterPosts: RecyclerViewAdapterPosts = RecyclerViewAdapterPosts()
    private lateinit var binding: LayoutFragmentPostsDataBinding
    private val viewModelPost: ViewModelPost by activityViewModels()
    private val viewModelUser: ViewModelUser by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        initializeViews(inflater, container)
        attachObserver()
        return binding.root
    }

    private fun initializeViews(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_posts_data, container, false)
        initializeRecyclerView()
        initializeRecyclerViewAdapter()
    }

    private fun attachObserver() {
        val user = viewModelUser.user.value
        if (user != null) {
            viewModelPost.getFavoritePosts().observe(viewLifecycleOwner, Observer {
                recyclerViewAdapterPosts.addData(ArrayList(it))
            })
        }

    }

    private fun initializeRecyclerViewAdapter() {
        binding.recyclerViewPopularBooks.adapter = recyclerViewAdapterPosts
    }

    /**
     * This method initialises all the the recyclerView
     * with a recyclerView adapter, a layout manager
     * and an item decoration
     */
    private fun initializeRecyclerView() {
        val recyclerView = binding.recyclerViewPopularBooks
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
    }
}