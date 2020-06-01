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
import com.example.bookapp.Adapters.ChatsAdapter
import com.example.bookapp.R
import com.example.bookapp.databinding.LayoutFragmentChatsBinding
import com.example.bookapp.viewModels.ViewModelChat
import com.example.bookapp.viewModels.ViewModelUser

class ChatsFragment : Fragment() {
    private val viewModelUser: ViewModelUser by activityViewModels()
    private val viewModelChat: ViewModelChat by activityViewModels()

    private lateinit var binding: LayoutFragmentChatsBinding;
    private val chatsAdapter: ChatsAdapter by lazy {
        ChatsAdapter()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_chats, container, false)

        viewModelUser.user.value?.let {
            configureRecyclerView()
            viewModelChat.getUserChats(it).observe(viewLifecycleOwner, Observer { chats ->
                chatsAdapter.setData(ArrayList(chats))
            })
        }
        return binding.root
    }

    private fun configureRecyclerView() {
        binding.recyclerViewFriends.adapter = chatsAdapter
        binding.recyclerViewFriends.setHasFixedSize(true)
        binding.recyclerViewFriends.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
        binding.recyclerViewFriends.layoutManager = LinearLayoutManager(requireContext())
    }


}