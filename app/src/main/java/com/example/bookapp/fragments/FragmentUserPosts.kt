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
import com.example.bookapp.databinding.FragmentMyPostsBinding
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
class FragmentUserPosts : Fragment() {
    private lateinit var binding: FragmentMyPostsBinding
    private val recyclerViewAdapterPosts: RecyclerViewAdapterPosts by lazy {
        RecyclerViewAdapterPosts()
    }
    private val viewModelPost: ViewModelPost by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMyPostsBinding.inflate(inflater, container, false)
        initializeRecyclerView()


        attachObserver()
        // Inflate the layout for this fragment
        return binding.root
    }


    private fun attachObserver() {

        viewModelPost.userPosts.observe(viewLifecycleOwner,
                Observer {
                        recyclerViewAdapterPosts.setData(it.posts)
                })
    }

    private fun initializeRecyclerView() {
        with(binding.recyclerViewMyPosts) {
            adapter = recyclerViewAdapterPosts
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
        }
    }
}