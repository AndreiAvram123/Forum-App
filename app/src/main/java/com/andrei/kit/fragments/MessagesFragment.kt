package com.andrei.kit.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.dataLayer.engineUtils.Result
import com.andrei.kit.Adapters.MessageAdapter
import com.andrei.kit.databinding.MessagesFragmentBinding
import com.andrei.kit.models.User
import com.andrei.kit.viewModels.ViewModelChat
import com.andrei.dataLayer.models.serialization.SerializeMessage
import com.andrei.dataLayer.serverConstants.MessageTypes
import com.andrei.kit.Adapters.CustomDivider
import com.andrei.kit.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.aprilapps.easyphotopicker.*
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MessagesFragment : Fragment() {

    private lateinit var binding: MessagesFragmentBinding
    private val viewModelChat: ViewModelChat by activityViewModels()

    @Inject
    lateinit var user: User

    @Inject
    lateinit var easyImage: EasyImage

    @Inject
    lateinit var permissionManager:PermissionManager

    private  val messageAdapter: MessageAdapter by lazy {
        MessageAdapter(user, ::expandImage)
    }

    private val args: MessagesFragmentArgs by navArgs()

    private val requestCodeCamera = 1


    private val cachedImages = LinkedList<MediaFile>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MessagesFragmentBinding.inflate(inflater, container, false)


        configureViews()

        viewModelChat.currentChatId.value = args.chatID

        viewModelChat.recentMessages.reObserve(viewLifecycleOwner, {
            //there is a small async problem once we start observing the recent
            //messages, we need to make sure that the live data had enough time to change the
            //messages from one chat id to another
            if (it.isNotEmpty() && it.first().chatID == args.chatID) {
                val ordered = it.reversed()
                messageAdapter.setData(ordered)
            }
        })
        viewModelChat.fetchNewMessages(args.chatID).observeRequest(viewLifecycleOwner, {
            when (it) {
                is Result.Success ->{
                    Snackbar.make(binding.root, "New messages fetched" ,Snackbar.LENGTH_LONG).show()
                }
                is Result.Loading->{

                }
                is Result.Error->{
                    Snackbar.make(binding.root, "Failed to get messages",Snackbar.LENGTH_LONG).show()
                }
                
            }
        })


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        messageAdapter.clear()
    }

    private fun expandImage(imageURL: String, localImage: Boolean) {
        val action = MessagesFragmentDirections.actionGlobalImageZoomFragment(imageURL, localImage)
        binding.root.findNavController().navigate(action)
    }

   private fun openCamera(){
       easyImage.openCameraForImage(this)
   }

    private fun configureViews() {
        configureRecyclerView()
        binding.sendButton.setOnClickListener {
            sendTextMessage()
        }
        binding.sendImageButton.setOnClickListener {
            easyImage.openGallery(this)
        }
        binding.captureImageButton.setOnClickListener {
            if (permissionManager.hasPermission(android.Manifest.permission.CAMERA)) {
                openCamera()
            } else {
                this.requestSinglePermission(permission = android.Manifest.permission.CAMERA,
                        requestCode = requestCodeCamera)

            }
        }

    }

    private fun sendTextMessage() {
        val text = binding.messageArea.text
        if (text != null) {
            val messageContent = text.toString()
            if (messageContent.trim().isNotEmpty()) {

                val message = SerializeMessage(type = MessageTypes.textMessage,
                        chatID = args.chatID, senderID = user.userID, content = messageContent)

                viewModelChat.sendMessage(message).observeRequest(viewLifecycleOwner, {
                    when (it) {
                        is Result.Success -> {
                        }
                        is Result.Loading -> {

                        }
                        is Result.Error -> {
                        }

                    }
                })
                text.clear()
            }
        }
    }



    private fun configureRecyclerView() {
        with(binding.recyclerViewMessages) {
            adapter = messageAdapter
            addItemDecoration(CustomDivider(10))
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            requestCodeCamera ->{
                if(grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED){
                  openCamera()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(), object : DefaultCallback() {
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                cachedImages.addAll(imageFiles)
            }

            override fun onImagePickerError(error: Throwable, source: MediaSource) {
                //Some error handling
                error.printStackTrace()
            }

            override fun onCanceled(@NonNull source: MediaSource) {
                //Not necessary to remove any files manually anymore
            }
        })
    }

    override fun onResume() {
        super.onResume()
        pushImageFromQueue()
    }



    private fun pushImageFromQueue() {

        cachedImages.poll()?.let{
            val drawable = it.file.toUri().toDrawable(requireContext())
            val message = SerializeMessage(
                    type = MessageTypes.imageMessageType,
                    chatID = args.chatID,
                    senderID = user.userID,
                    content = drawable.toBase64()

            )
            viewModelChat.sendMessage(message).observeRequest(viewLifecycleOwner, { result ->
                when (result) {
                   is  Result.Loading -> {

                    }
                   is  Result.Success -> {
                        pushImageFromQueue()
                    }
                    else -> {

                    }
                }
            })
        }


    }
}