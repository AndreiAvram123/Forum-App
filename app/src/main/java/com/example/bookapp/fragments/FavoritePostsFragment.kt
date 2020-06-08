package com.example.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.Adapters.RecyclerViewAdapterPosts
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentFavoritePostsBinding
import com.example.bookapp.models.Post
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
class FavoritePostsFragment : Fragment() {
    private val recyclerViewAdapterPosts: RecyclerViewAdapterPosts? = null
    private val viewModelPost: ViewModelPost by activityViewModels()
    private val viewModelUser: ViewModelUser by activityViewModels()
    private val favoritePosts: ArrayList<Post>? = null
    private lateinit var binding: FragmentFavoritePostsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritePostsBinding.inflate(inflater, container, false)
        initializeRecyclerView()
        attachObserver()
        return binding.root
    }


    private fun attachObserver() {

        val user = viewModelUser.user.value
        if (user != null) {
            viewModelPost.getFavoritePosts().observe(viewLifecycleOwner, Observer {
                if (it.isNullOrEmpty()) {
                    recyclerViewAdapterPosts!!.setData(ArrayList(it))
                    binding.numberResults.text = getString(R.string.number_saved_posts, it.size)
                }
            })
        }
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