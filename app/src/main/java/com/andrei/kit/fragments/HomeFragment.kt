package com.andrei.kit.fragments

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.kit.Adapters.CustomDivider
import com.andrei.kit.Adapters.HomeAdapter
import com.andrei.kit.R
import com.andrei.kit.databinding.LayoutHomeFragmentBinding
import com.andrei.kit.utils.reObserve
import com.andrei.kit.viewModels.ViewModelPost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: LayoutHomeFragmentBinding
    private val viewModelPost: ViewModelPost by activityViewModels()

    @Inject
    lateinit var connectivityManager: ConnectivityManager


    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter(
                connectivityManager = connectivityManager,
                viewModelPost = viewModelPost)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_home_fragment, container, false);
        initializeUI()
        viewModelPost.recentPosts.reObserve(viewLifecycleOwner, {
            homeAdapter.submitList(it)
        })
        return binding.root
    }


    private fun initializeUI() {
        binding.homeSwipeRefreshLayout.setOnRefreshListener {
            viewModelPost.refreshPostData()
            binding.homeSwipeRefreshLayout.isRefreshing = false
        }
        initializeRecyclerView()
        binding.addPostButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeToFragmentAddPost()
            findNavController().navigate(action)
        }
    }

    private fun initializeRecyclerView() {
        binding.recyclerViewHome.apply {
            adapter = homeAdapter
            addItemDecoration(CustomDivider(100))
            layoutManager = LinearLayoutManager(requireContext())
        }
    }



}