package com.example.bookapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.Adapters.CustomDivider
import com.example.bookapp.Adapters.MessageAdapter
import com.example.bookapp.databinding.MessagesFragmentBinding
import com.example.bookapp.models.MessageDTO
import com.example.bookapp.viewModels.ViewModelChat
import com.example.bookapp.viewModels.ViewModelUser
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import okhttp3.internal.closeQuietly
import org.json.JSONObject
import java.net.URI
import java.time.Duration

class MessagesFragment : Fragment() {
    private lateinit var binding: MessagesFragmentBinding
    private val viewModelUser: ViewModelUser by activityViewModels()
    private val viewModelChat: ViewModelChat by activityViewModels()
    private var eventSource: EventSource? = null
    private lateinit var adapter: MessageAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MessagesFragmentBinding.inflate(inflater, container, false)

        viewModelChat.chatID.value = 1

        viewModelUser.user.value?.let {
            adapter = MessageAdapter(it)
            configureViews()
            viewModelChat.recentMessages.observe(viewLifecycleOwner, Observer { data ->
                adapter.addNewMessages(data)
            })
            viewModelChat.chatLink.observe(viewLifecycleOwner, Observer { chatLink ->
                configureServerEvents(chatLink.hubURL)
            })

        }
        return binding.root
    }


    override fun onStop() {
        super.onStop()
        eventSource?.closeQuietly()
        Log.d(MessagesFragment::class.simpleName, "closing server side event")
    }


    private fun configureServerEvents(url: String) {
        val eventHandler: EventHandler = object : EventHandler {
            override fun onOpen() {
                Log.d(MessagesFragment::class.simpleName, "Server side event opened")
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
                            requireActivity().runOnUiThread {
                                adapter.add(message)
                            }
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
            //"http://www.andreiram.co.uk:3000/.well-known/mercure?topic=/chat"
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

    private fun configureViews() {
        configureRecyclerView()
        binding.sendButton.setOnClickListener {
            val text = binding.messageArea.text;
            if (text != null) {
                val messageContent = binding.messageArea.text.toString().trim { it <= ' ' }
                if (messageContent.trim { it <= ' ' } != "") {
                    viewModelUser.user.value?.let {
                        viewModelChat.sendMessage(chatID = 1, senderID = it.userID, content = messageContent)
                    }
                    text.clear()
                }
            }
        }
    }


    private fun configureRecyclerView() {
        binding.recyclerViewMessages.adapter = adapter
        binding.recyclerViewMessages.addItemDecoration(CustomDivider(10))
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(requireContext())
    }
}