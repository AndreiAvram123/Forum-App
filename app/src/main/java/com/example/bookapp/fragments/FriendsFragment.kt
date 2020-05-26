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
import com.example.bookapp.Adapters.FriendsAdapter
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentFriendsBinding
import com.example.bookapp.interfaces.MainActivityInterface
import com.example.bookapp.viewModels.ViewModelFriends
import com.example.bookapp.viewModels.ViewModelUser

class FriendsFragment : Fragment() {
    private val viewModelFriends: ViewModelFriends by activityViewModels()
    private val viewModelUser: ViewModelUser by activityViewModels()

    private lateinit var binding: FragmentFriendsBinding;
    private val friendsAdapter: FriendsAdapter by lazy {
        FriendsAdapter(requireActivity() as MainActivityInterface)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false)
        //set the adapter
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
        val currentUser = viewModelUser.user.value
        viewModelUser.user.value?.let { user ->
            viewModelFriends.getFriends(user).observe(viewLifecycleOwner, Observer {
                it?.let { friendsAdapter.setData(it) }
            })
        }
    }
}