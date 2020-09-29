package com.andrew.bookapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrew.bookapp.Adapters.CustomDivider
import com.andrew.bookapp.Adapters.MessageAdapter
import com.andrew.bookapp.databinding.MessagesFragmentBinding
import com.andrew.bookapp.models.LocalImageMessage
import com.andrew.bookapp.models.User
import com.andrew.bookapp.toBase64
import com.andrew.bookapp.toDrawable
import com.andrew.bookapp.viewModels.ViewModelChat
import com.andrew.dataLayer.dataMappers.UserMapper
import com.andrew.dataLayer.models.serialization.SerializeMessage
import com.andrew.dataLayer.serverConstants.MessageTypes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@InternalCoroutinesApi
@AndroidEntryPoint
class MessagesFragment : Fragment() {
    private lateinit var binding: MessagesFragmentBinding
    private val viewModelChat: ViewModelChat by activityViewModels()

    @Inject
    lateinit var user: User

    private lateinit var messageAdapter: MessageAdapter
    private val args: MessagesFragmentArgs by navArgs()
    private val codeFileExplorer = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MessagesFragmentBinding.inflate(inflater, container, false)


        messageAdapter = MessageAdapter(user, ::expandImage)
        configureViews()

        viewModelChat.currentChatId.value = args.chatID

        viewModelChat.recentMessages.observe(viewLifecycleOwner, Observer {
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
        binding.messageArea.text?.let {
            val messageContent = binding.messageArea.text.toString()
            if (messageContent.trim().isNotEmpty()) {

                val message = SerializeMessage(type = MessageTypes.textMessage,
                        chatID = args.chatID, senderID = user.userID, content = messageContent, localIdentifier = null)
                viewModelChat.sendMessage(message)
                it.clear()
            }
        }
    }

    private fun startFileExplorer() {
        val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
        fileIntent.type = "image/*"
        startActivityForResult(fileIntent, codeFileExplorer)
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
        if (requestCode == codeFileExplorer) {
            data?.let {
                val path = it.data
                if (path != null) {
                    pushImage(path)
                }
            }
        }
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
                viewModelChat.sendMessage(message)

                val localImageMessage = LocalImageMessage(
                        sender = UserMapper.mapDomainToNetworkObject(user),
                        type = MessageTypes.imageMessageType,
                        localID = uniqueID,
                        resourcePath = path
                )
                messageAdapter.add(localImageMessage)
            }
        }
    }
}