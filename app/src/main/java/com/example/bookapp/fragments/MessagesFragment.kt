package com.example.bookapp.fragments

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
import com.example.bookapp.Adapters.CustomDivider
import com.example.bookapp.Adapters.MessageAdapter
import com.example.bookapp.AppUtilities
import com.example.bookapp.databinding.MessagesFragmentBinding
import com.example.bookapp.models.LocalImageMessage
import com.example.bookapp.models.Message
import com.example.bookapp.models.MessageDTO
import com.example.bookapp.models.User
import com.example.bookapp.viewModels.ViewModelChat
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.dataMappers.UserMapper
import com.example.dataLayer.models.serialization.SerializeMessage
import com.example.dataLayer.serverConstants.MessageTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*


@InternalCoroutinesApi
class MessagesFragment : Fragment() {
    private lateinit var binding: MessagesFragmentBinding
    private val viewModelUser: ViewModelUser by activityViewModels()
    private val viewModelChat: ViewModelChat by activityViewModels()

    private lateinit var messageAdapter: MessageAdapter
    private val args: MessagesFragmentArgs by navArgs()
    private val codeFileExplorer = 10
    private lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MessagesFragmentBinding.inflate(inflater, container, false)
        user = viewModelUser.user


        messageAdapter = MessageAdapter(user, ::expandImage)
        configureViews()
        viewModelChat.chatID.value = args.chatID


        viewModelChat.recentMessages.observe(viewLifecycleOwner, Observer {
            messageAdapter.setData(it.reversed())
            if (!it.last().seenByCurrentUser) {
                viewModelChat.markMessageAsSeen(it.last(), user)
            }
        })


        binding.sendImageButton.setOnClickListener {
            startFileExplorer()
        }

        return binding.root
    }


    private fun expandImage(imageURL: String, localImage: Boolean) {
        val action = MessagesFragmentDirections.actionMessagesFragmentToImageZoomFragment(imageURL, localImage)
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
        val text = binding.messageArea.text;
        if (text != null) {
            val messageContent = binding.messageArea.text.toString()
            if (messageContent.trim().isNotEmpty()) {

                val message = SerializeMessage(type = MessageTypes.textMessage,
                        chatID = args.chatID, senderID = user.userID, content = messageContent, localIdentifier = null)
                viewModelChat.sendMessage(message)
                text.clear()
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
        val drawable = AppUtilities.convertFromUriToDrawable(path, requireContext())

        lifecycleScope.launch(Dispatchers.IO) {
            val data = AppUtilities.getBase64ImageFromDrawable(drawable)

            val uniqueID = Calendar.getInstance().timeInMillis.hashCode().toString()
            lifecycleScope.launch(Dispatchers.Main) {
                val message = SerializeMessage(
                        type = MessageTypes.imageMessageType,
                        chatID = args.chatID,
                        senderID = user.userID,
                        content = data,
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