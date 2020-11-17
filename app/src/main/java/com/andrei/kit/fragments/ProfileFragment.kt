package com.andrei.kit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.andrei.dataLayer.models.deserialization.FriendRequest
import com.andrei.dataLayer.models.serialization.SerializeFriendRequest
import com.andrei.kit.R
import com.andrei.kit.databinding.FragmentProfileBinding
import com.andrei.kit.models.User
import com.andrei.kit.models.UsersStatus
import com.andrei.kit.utils.observeRequest
import com.andrei.kit.utils.reObserve
import com.andrei.kit.viewModels.ViewModelChat
import com.andrei.kit.viewModels.ViewModelUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.andrei.dataLayer.engineUtils.Result

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var user: User

    private lateinit var binding:FragmentProfileBinding

    private val viewModelChat:ViewModelChat by activityViewModels()
    private val viewModelUser:ViewModelUser by activityViewModels()

    private val navArgs:ProfileFragmentArgs  by navArgs()

    private var receivedFriendRequest:FriendRequest? = null

    private  var usersStatus: UsersStatus = UsersStatus.NOT_FRIENDS
    set(value) {
        field= value
        modifyButtonState()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        viewModelUser.getUser(navArgs.userID).reObserve(viewLifecycleOwner,{
                   if(it!=null) {
                       binding.user = it
                   }
        })

        //check to see if the users are friends
        viewModelUser.friends.reObserve(viewLifecycleOwner,{
            if(!it.isNullOrEmpty()){
                it.find { user -> user.userID == navArgs.userID }?.let {
                    usersStatus = UsersStatus.FRIENDS
                }
            }
        })

        viewModelChat.allFriendRequests.reObserve(viewLifecycleOwner,{ friendRequests ->
            //check to see if the user logged in sent an invitation to the other user
            friendRequests.find { it.receiver.userID == navArgs.userID }?.let{
               usersStatus = UsersStatus.FRIEND_INVITATION_SENT
           }

            //check to see whether the current logged in user received an invitation
            friendRequests.filter { it.receiver.userID == user.userID  }
                    .find { it.sender.userID == navArgs.userID}?.let {
                    receivedFriendRequest = it
                    usersStatus =   UsersStatus.FRIEND_INVITATION_RECEIVED

            }
        })
        modifyButtonState()
        return binding.root
    }

    private fun modifyButtonState() {
        when (usersStatus) {
            UsersStatus.FRIEND_INVITATION_SENT -> {
                binding.addToFriendsButton.apply {
                    text = getString(R.string.invitation_sent)
                    isClickable = false
                }
            }
            UsersStatus.FRIEND_INVITATION_RECEIVED ->{
                binding.addToFriendsButton.apply {
                    isClickable = true
                    text = getString(R.string.accept_invitation)
                    setOnClickListener {
                        receivedFriendRequest?.let {  acceptInvitation(it)}
                    }
                }
            }
            UsersStatus.FRIENDS ->{
                binding.addToFriendsButton.visibility = View.INVISIBLE
            }
            UsersStatus.NOT_FRIENDS ->{
                binding.addToFriendsButton.setOnClickListener {
                    sendInvitation()
                }
            }
        }
    }


    private fun acceptInvitation(friendRequest:FriendRequest){
        viewModelChat.acceptFriendRequest(friendRequest).observeRequest(viewLifecycleOwner,{
            if(it is Result.Success){
                binding.addToFriendsButton.apply {
                  usersStatus = UsersStatus.FRIENDS
                }
            }
        })
    }
    private fun sendInvitation() {
            val request = SerializeFriendRequest(senderID = user.userID,receiverID = navArgs.userID)
            viewModelChat.sendFriendRequest(request).observeRequest(viewLifecycleOwner, {
               if(it is Result.Error){
                   //hmm not sure what to do here
               }
            })
        }

}