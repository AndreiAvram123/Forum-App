package com.andrei.kit.fragments

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.kit.Adapters.SimpleAdapterPosts
import com.andrei.kit.R
import com.andrei.kit.databinding.FragmentFavoritePostsBinding
import com.andrei.kit.utils.reObserve
import com.andrei.kit.viewModels.ViewModelPost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite_posts.*
import kotlinx.android.synthetic.main.fragment_favorite_posts.view.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class FavoritePostsFragment : Fragment() {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    private val simpleAdapterPosts: SimpleAdapterPosts by lazy {
        SimpleAdapterPosts(connectivityManager)
    }
    private val viewModelPost: ViewModelPost by activityViewModels()
    private lateinit var binding: FragmentFavoritePostsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritePostsBinding.inflate(inflater, container, false)
        initializeRecyclerView()
         viewModelPost.getFavoritePosts().reObserve(viewLifecycleOwner,{
             simpleAdapterPosts.setData(it.toMutableList())
         })
        return binding.root
    }


    private fun initializeRecyclerView() {
       binding.recyclerViewFavoritePosts.apply {
            adapter = simpleAdapterPosts
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
        }
    }
}