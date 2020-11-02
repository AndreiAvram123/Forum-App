package com.andrei.kit.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.kit.Adapters.MessageAdapter
import com.andrei.kit.databinding.MessagesFragmentBinding
import com.andrei.kit.models.LocalImageMessage
import com.andrei.kit.models.User
import com.andrei.kit.observeRequest
import com.andrei.kit.toBase64
import com.andrei.kit.toDrawable
import com.andrei.kit.viewModels.ViewModelChat
import com.andrei.dataLayer.dataMappers.toNetworkObject
import com.andrei.dataLayer.engineUtils.Status
import com.andrei.dataLayer.models.serialization.SerializeMessage
import com.andrei.dataLayer.serverConstants.MessageTypes
import com.andrei.kit.Adapters.CustomDivider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.aprilapps.easyphotopicker.*
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MessagesFragment : Fragment() {
    private lateinit var binding: MessagesFragmentBinding
    private val viewModelChat: ViewModelChat by activityViewModels()

    @Inject
    lateinit var user: User

    private lateinit var messageAdapter: MessageAdapter
    private val args: MessagesFragmentArgs by navArgs()
    private var easyImage:EasyImage? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MessagesFragmentBinding.inflate(inflater, container, false)


        messageAdapter = MessageAdapter(user, ::expandImage)
        configureViews()

        viewModelChat.currentChatId.value = args.chatID

        viewModelChat.recentMessages.observe(viewLifecycleOwner, {
            //there is a small async problem once we start observing the recent
            //messages, we need to make sure that the live data had enough time to change the
            //messages from one chat id to another
            if (it.isNotEmpty() && it.first().chatID == args.chatID) {
                val ordered = it.reversed()
                messageAdapter.setData(ordered)
                if (!ordered.last().seenByCurrentUser) {
                    viewModelChat.markMessageAsSeen(ordered.last(), user)
                }
            }
        })
        viewModelChat.fetchNewMessages().observeRequest(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {

                }

                Status.SUCCESS -> {
                }
                else -> {

                }
            }
        })

        binding.sendImageButton.setOnClickListener {
            startFileExplorer()
        }

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


    private fun configureViews() {
        configureRecyclerView()
        binding.sendButton.setOnClickListener {
            sendTextMessage()
        }
        binding.sendImageButton.setOnClickListener {
            startFileExplorer()
        }
    }

    private fun sendTextMessage() {
        val text = binding.messageArea.text
        if( text != null)  {
            val messageContent = text.toString()
            if (messageContent.trim().isNotEmpty()) {

                val message = SerializeMessage(type = MessageTypes.textMessage,
                        chatID = args.chatID, senderID = user.userID, content = messageContent, localIdentifier = null)

                viewModelChat.sendMessage(message).observeRequest(viewLifecycleOwner, {
                    when (it.status) {
                        Status.LOADING -> {

                        }
                        Status.SUCCESS -> {

                        }
                        Status.ERROR -> {

                        }
                    }
                })
                text.clear()
            }
        }
    }

    private fun startFileExplorer() {
         easyImage = EasyImage.Builder(requireContext()) // Chooser only
                // Will appear as a system chooser title, DEFAULT empty string
                //.setChooserTitle("Pick media")
                // Will tell chooser that it should show documents or gallery apps
                //.setChooserType(ChooserType.CAMERA_AND_DOCUMENTS)  you can use this or the one below
                //.setChooserType(ChooserType.CAMERA_AND_GALLERY)
                // Setting to true will cause taken pictures to show up in the device gallery, DEFAULT false
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setCopyImagesToPublicGalleryFolder(true) // Sets the name for images stored if setCopyImagesToPublicGalleryFolder = true
                .setFolderName("EasyImage sample") // Allow multiple picking
                .allowMultiple(true)
                .build()
        easyImage?.openGallery(this)
    }


    private fun configureRecyclerView() {
        with(binding.recyclerViewMessages) {
            adapter = messageAdapter
            addItemDecoration(CustomDivider(10))
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage?.handleActivityResult(requestCode, resultCode, data, requireActivity(), object : DefaultCallback() {
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                data?.let {
                    val path = it.data
                    if (path != null) {
                        pushImage(path)
                    }
                }
            }

            override fun onImagePickerError(error: Throwable,  source: MediaSource) {
                //Some error handling
                error.printStackTrace()
            }

            override fun onCanceled(@NonNull source: MediaSource) {
                //Not necessary to remove any files manually anymore
            }
        })
    }

    private fun pushImage(path: Uri) {
        val drawable = path.toDrawable(requireContext())

        lifecycleScope.launch(Dispatchers.IO) {

            val uniqueID = Calendar.getInstance().timeInMillis.hashCode().toString()

            lifecycleScope.launch(Dispatchers.Main) {
                val message = SerializeMessage(
                        type = MessageTypes.imageMessageType,
                        chatID = args.chatID,
                        senderID = user.userID,
                        content = drawable.toBase64(),
                        localIdentifier = uniqueID

                )
                viewModelChat.sendMessage(message).observe(viewLifecycleOwner, {
                    when (it.status) {
                        Status.LOADING -> {

                        }
                        Status.SUCCESS -> {
                            val localImageMessage = LocalImageMessage(
                                    sender = user.toNetworkObject(),
                                    type = MessageTypes.imageMessageType,
                                    localID = uniqueID,
                                    resourcePath = path
                            )
                            messageAdapter.add(localImageMessage)
                        }
                        else -> {

                        }
                    }

                })
            }
        }
    }
}