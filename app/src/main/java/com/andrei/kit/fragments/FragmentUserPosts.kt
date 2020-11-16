package com.andrei.kit.fragments

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.kit.Adapters.SimpleAdapterPosts
import com.andrei.kit.databinding.FragmentMyPostsBinding
import com.andrei.kit.utils.reObserve
import com.andrei.kit.viewModels.ViewModelPost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class FragmentUserPosts : Fragment() {
    @Inject
     lateinit var connectivityManager: ConnectivityManager

    private lateinit var binding: FragmentMyPostsBinding
    private val simpleAdapterPosts: SimpleAdapterPosts by lazy {
        SimpleAdapterPosts(connectivityManager)
    }
    private val viewModelPost: ViewModelPost by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMyPostsBinding.inflate(inflater, container, false)
        initializeRecyclerView()

        viewModelPost.userPosts.reObserve(viewLifecycleOwner,
                {
                    simpleAdapterPosts.setData(it.posts)
                })
        // Inflate the layout for this fragment
        return binding.root
    }



    private fun initializeRecyclerView() {
         binding.recyclerViewMyPosts.apply {
            adapter = simpleAdapterPosts
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
        }
    }
}