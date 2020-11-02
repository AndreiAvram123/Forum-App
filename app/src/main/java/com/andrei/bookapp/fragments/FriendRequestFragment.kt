package com.andrei.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.bookapp.Adapters.CustomDivider
import com.andrei.bookapp.databinding.FragmentFriendRequestBinding
import com.andrei.bookapp.viewModels.ViewModelChat
import com.andrei.dataLayer.models.deserialization.FriendRequest


class FriendRequestFragment : Fragment() {

    private val viewModelChat: ViewModelChat by activityViewModels()

    private val requestAdapter = FriendRequestsAdapter(::acceptRequest)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentFriendRequestBinding.inflate(inflater, container, false)

        with(binding.friendRequestsList) {
            adapter = requestAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(CustomDivider(20))
        }

        viewModelChat.refreshFriendRequests()
        viewModelChat.friendRequests.observe(viewLifecycleOwner, Observer {
            requestAdapter.setData(it)
        })
        return binding.root
    }

    private fun acceptRequest(request: FriendRequest) {
        viewModelChat.acceptFriendRequest(request)
    }
}