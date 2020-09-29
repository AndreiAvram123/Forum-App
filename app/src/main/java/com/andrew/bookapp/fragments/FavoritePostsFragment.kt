package com.andrew.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrew.bookapp.Adapters.RecyclerViewAdapterPosts
import com.andrew.bookapp.R
import com.andrew.bookapp.databinding.FragmentFavoritePostsBinding
import com.andrew.bookapp.viewModels.ViewModelPost
import com.andrew.bookapp.viewModels.ViewModelUser
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
class FavoritePostsFragment : Fragment() {
    private val recyclerViewAdapterPosts: RecyclerViewAdapterPosts by lazy {
        RecyclerViewAdapterPosts()
    }
    private val viewModelPost: ViewModelPost by activityViewModels()
    private val viewModelUser: ViewModelUser by activityViewModels()
    private lateinit var binding: FragmentFavoritePostsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritePostsBinding.inflate(inflater, container, false)
        initializeRecyclerView()
        attachObserver()
        return binding.root
    }


    private fun attachObserver() {

            viewModelPost.getFavoritePosts().observe(viewLifecycleOwner, Observer {
                recyclerViewAdapterPosts.setData(ArrayList(it.posts))
                binding.numberResults.text = getString(R.string.number_saved_posts, it.posts.size)
            })
    }


    /**
     * This method initialises all the the recyclerView
     * with a recyclerView adapter, a layout manager
     * and an item decoration
     */
    private fun initializeRecyclerView() {
        with(binding.recyclerViewFavoritePosts) {
            adapter = recyclerViewAdapterPosts
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
        }
    }
}