package com.example.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.Adapters.FriendsAdapter
import com.example.bookapp.R
import com.example.bookapp.databinding.LayoutFragmentChatsBinding
import com.example.bookapp.viewModels.ViewModelUser

class ChatsFragment : Fragment() {
    private val viewModelUser: ViewModelUser by activityViewModels()

    private lateinit var binding: LayoutFragmentChatsBinding;
    private lateinit var friendsAdapter: FriendsAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_chats, container, false)

        friendsAdapter = FriendsAdapter(this.findNavController())
        configureRecyclerView()
        attachObservers()
        return binding.root
    }

    private fun configureRecyclerView() {
        binding.recyclerViewFriends.adapter = friendsAdapter
        binding.recyclerViewFriends.setHasFixedSize(true)
        binding.recyclerViewFriends.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
        binding.recyclerViewFriends.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun attachObservers() {
        viewModelUser.friends.observe(viewLifecycleOwner, Observer {
            friendsAdapter.setData(ArrayList(it))
        })
    }
}