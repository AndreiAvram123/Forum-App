package com.andrei.kit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.andrei.dataLayer.engineUtils.Status
import com.andrei.dataLayer.models.serialization.SerializeFriendRequest
import com.andrei.kit.R
import com.andrei.kit.databinding.FragmentProfileBinding
import com.andrei.kit.models.User
import com.andrei.kit.utils.observeOnceForValue
import com.andrei.kit.utils.observeRequest
import com.andrei.kit.utils.reObserve
import com.andrei.kit.viewModels.ViewModelChat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var user: User

    private lateinit var binding:FragmentProfileBinding

    private val viewModelChat:ViewModelChat by activityViewModels()

    private val userID = "df"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        viewModelChat.allFriendRequests.reObserve(viewLifecycleOwner,{ friendRequests ->
            friendRequests.find { it.receiver.userID == userID }
        })

//        binding.addToFriendsButton.setOnClickListener{
//            val request = SerializeFriendRequest(senderID = user.userID,receiverID = userID)
//            viewModelChat.sendFriendRequest(request).observeRequest(viewLifecycleOwner, {
//               if(it.status == Status.SUCCESS){
//                   binding.addToFriendsButton.apply {
//                       isClickable = false
//                       text = getString(R.string.invitation_sent)
//                   }
//               }
//            })
//        }
        return binding.root
    }

}