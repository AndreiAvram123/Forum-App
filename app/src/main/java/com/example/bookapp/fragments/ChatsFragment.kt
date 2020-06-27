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
import com.example.bookapp.Adapters.ChatsAdapter
import com.example.bookapp.databinding.LayoutFragmentChatsBinding
import com.example.bookapp.viewModels.ViewModelChat

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
        viewModelChat.userChats.observe(viewLifecycleOwner, Observer {
            chatsAdapter.setData(it)
            viewModelChat.unseenMessages.value?.let { ids->
                chatsAdapter.showNotifications(ids)
            }
        })
        viewModelChat.unseenMessages.observe(viewLifecycleOwner, Observer {
            chatsAdapter.showNotifications(it)
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