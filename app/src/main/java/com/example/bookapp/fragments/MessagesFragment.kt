package com.example.bookapp.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.UserManager
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
import com.example.bookapp.models.MessageDTO
import com.example.bookapp.models.User
import com.example.bookapp.viewModels.ViewModelChat
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.dataMappers.UserMapper
import com.example.dataLayer.models.UserDTO
import com.example.dataLayer.models.serialization.SerializeMessage
import com.example.dataLayer.serverConstants.MessageTypes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.closeQuietly
import org.json.JSONObject
import java.net.URI
import java.time.Duration
import java.util.*


class MessagesFragment : Fragment() {
    private lateinit var binding: MessagesFragmentBinding
    private val viewModelUser: ViewModelUser by activityViewModels()
    private val viewModelChat: ViewModelChat by activityViewModels()

    private var eventSource: EventSource? = null

    private lateinit var messageAdapter: MessageAdapter
    private val args: MessagesFragmentArgs by navArgs()
    private val codeFileExplorer = 10
    private lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MessagesFragmentBinding.inflate(inflater, container, false)

        viewModelUser.user.value?.let {
            user = it
        }

        messageAdapter = MessageAdapter(user, ::expandImage)
        configureViews()
        viewModelChat.chatID.value = args.chatID

        viewModelChat.recentMessages.observe(viewLifecycleOwner, Observer { data ->
            messageAdapter.addNewMessages(data)
        })

        viewModelChat.chatLink.observe(viewLifecycleOwner, Observer { chatLink ->
            if (eventSource == null) {
                configureServerEvents(chatLink.hubURL)
            }
        })


        binding.sendImageButton.setOnClickListener {
            startFileExplorer()
        }
        return binding.root
    }

    private fun expandImage(imageURL: String) {
        val action = MessagesFragmentDirections.actionMessagesFragmentToImageZoomFragment(imageURL)
        binding.root.findNavController().navigate(action)
    }

    override fun onDetach() {
        super.onDetach()
        eventSource?.closeQuietly()
    }

    private fun configureServerEvents(url: String) {
        val eventHandler: EventHandler = object : EventHandler {
            override fun onOpen() {
            }

            override fun onComment(comment: String?) {

            }

            override fun onMessage(event: String?, messageEvent: MessageEvent) {
                val gson: Gson = GsonBuilder().setPrettyPrinting().create()
                val data = messageEvent.data

                if (data != null) {
                    val jsonObject = JSONObject(data)

                    when (jsonObject.get("type")) {
                        "message" -> {
                            val message = gson.fromJson(jsonObject.get("message").toString(), MessageDTO::class.java)
                            addNewMessage(message)
                        }
                        else -> {

                        }
                    }
                }

            }

            override fun onClosed() {

            }

            override fun onError(t: Throwable?) {

            }
        }
        val event: EventSource.Builder = EventSource.Builder(
                eventHandler,
                URI.create(url)
        )
                .reconnectTime(Duration.ofMillis(10));
        val temp = event.build()
        eventSource = temp
        temp.start()
    }

    private fun addNewMessage(message: MessageDTO) {

        requireActivity().runOnUiThread {
            messageAdapter.add(message)
        }
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
                    val drawable = AppUtilities.convertFromUriToDrawable(path, requireContext())
                    pushImage(drawable)

                }
            }
        }
    }

    private fun pushImage(image: Drawable) {
        lifecycleScope.launch(Dispatchers.IO) {
            val data = AppUtilities.getBase64ImageFromDrawable(image)

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
                        id = -1,
                        sender = UserMapper.mapDomainToNetworkObject(user),
                        type = MessageTypes.imageMessageType,
                        localID = uniqueID,
                        drawable = image
                )
                messageAdapter.add(localImageMessage)
            }
        }
    }
}