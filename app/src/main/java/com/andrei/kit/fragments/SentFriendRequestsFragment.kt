package com.andrei.kit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.kit.Adapters.CustomDivider
import com.andrei.kit.databinding.LayoutFragmentFriendRequestBinding
import com.andrei.kit.models.User
import com.andrei.kit.utils.reObserve
import com.andrei.kit.viewModels.ViewModelChat
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SentFriendRequestsFragment private constructor(): Fragment() {

    private val viewModelChat: ViewModelChat by activityViewModels()

    @Inject
    lateinit var user:User
    private val requestAdapter by lazy {
        FriendRequestsAdapter(currentUser = user)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = LayoutFragmentFriendRequestBinding.inflate(inflater, container, false)

        binding.friendRequestsList.apply {
            adapter = requestAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(CustomDivider(20))
        }

        viewModelChat.sentFriendRequests.reObserve(viewLifecycleOwner, {
            if(!it.isNullOrEmpty()) {
                requestAdapter.setData(it)
            }
        })
        return binding.root
    }
    companion object{
        @JvmStatic
        fun getInstance() = SentFriendRequestsFragment()
    }

}