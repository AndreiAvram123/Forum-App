package com.andrei.kit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.kit.Adapters.ChatsAdapter
import com.andrei.kit.databinding.LayoutFragmentChatsBinding
import com.andrei.kit.viewModels.ViewModelChat

class ChatsFragment : Fragment() {
    private val viewModelChat: ViewModelChat by activityViewModels()

    private lateinit var binding: LayoutFragmentChatsBinding
    private val chatsAdapter: ChatsAdapter by lazy {
        ChatsAdapter()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = LayoutFragmentChatsBinding.inflate(inflater, container, false)
        configureRecyclerView()
        viewModelChat.userChats.observe(viewLifecycleOwner, {
            chatsAdapter.setData(it)
        })
        return binding.root
    }

    private fun configureRecyclerView() {
        with(binding.recyclerViewFriends) {
            adapter = chatsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


}