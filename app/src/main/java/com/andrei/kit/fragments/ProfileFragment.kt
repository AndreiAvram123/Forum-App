package com.andrei.kit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.andrei.dataLayer.engineUtils.Status
import com.andrei.dataLayer.models.deserialization.FriendRequest
import com.andrei.dataLayer.models.deserialization.FriendRequestStatus
import com.andrei.dataLayer.models.serialization.SerializeFriendRequest
import com.andrei.kit.R
import com.andrei.kit.databinding.FragmentProfileBinding
import com.andrei.kit.models.User
import com.andrei.kit.utils.observeOnceForValue
import com.andrei.kit.utils.observeRequest
import com.andrei.kit.utils.reObserve
import com.andrei.kit.viewModels.ViewModelAuth
import com.andrei.kit.viewModels.ViewModelChat
import com.andrei.kit.viewModels.ViewModelUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var user: User

    private lateinit var binding:FragmentProfileBinding

    private val viewModelChat:ViewModelChat by activityViewModels()
    private val viewModelUser:ViewModelUser by activityViewModels()

    private val navArgs:ProfileFragmentArgs  by navArgs()


    private  var friendRequestStatus: FriendRequestStatus = FriendRequestStatus.ACCEPTED

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        viewModelUser.getUser(navArgs.userID).reObserve(viewLifecycleOwner,{
                   if(it!=null) {
                       binding.user = it
                   }
        })

        viewModelChat.allFriendRequests.reObserve(viewLifecycleOwner,{ friendRequests ->

            friendRequests.find { it.receiver.userID == navArgs.userID }?.let{
               friendRequestStatus = FriendRequestStatus.SENT
               modifyButtonState(it)
           }
            friendRequests.filter { it.receiver.userID == user.userID  }
                    .find { it.sender.userID == navArgs.userID}?.let {
                    friendRequestStatus =   FriendRequestStatus.RECEIVED
                        modifyButtonState(it)
            }
        })

        return binding.root
    }

    private fun modifyButtonState(friendRequest: FriendRequest) {
        when (friendRequestStatus) {

            FriendRequestStatus.SENT -> {
                binding.addToFriendsButton.apply {
                    text = getString(R.string.invitation_sent)
                }
            }
            FriendRequestStatus.RECEIVED ->{
                binding.addToFriendsButton.apply {
                    isClickable = true
                    text = getString(R.string.accept_invitation)
                    setOnClickListener {
                        acceptInvitation(friendRequest)
                    }
                }
            }
            else -> {

            }
        }
    }


    private fun acceptInvitation(friendRequest:FriendRequest){
        viewModelChat.acceptFriendRequest(friendRequest).observeRequest(viewLifecycleOwner,{
            if(it.status == Status.SUCCESS){
                binding.addToFriendsButton.apply {

                }
            }
        })
    }
    private fun sendInvitation() {
            val request = SerializeFriendRequest(senderID = user.userID,receiverID = navArgs.userID)
            viewModelChat.sendFriendRequest(request).observeRequest(viewLifecycleOwner, {
               if(it.status == Status.SUCCESS){
                   binding.addToFriendsButton.apply {
                       isClickable = false
                       text = getString(R.string.invitation_sent)
                   }
               }
            })
        }

}