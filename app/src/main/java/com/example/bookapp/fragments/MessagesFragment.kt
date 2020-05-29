package com.example.bookapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.Adapters.AdapterMessages
import com.example.bookapp.R
import com.example.bookapp.databinding.MessagesFragmentBinding
import com.example.bookapp.viewModels.ViewModelMessages
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import java.net.URI
import java.time.Duration

class MessagesFragment : Fragment() {
    private lateinit var binding: MessagesFragmentBinding
    private val viewModelMessages: ViewModelMessages? = null
    private lateinit var adapterMessages: AdapterMessages
    private lateinit var eventSource: EventSource

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.messages_fragment, container, false)
        //todo
        //change this
        adapterMessages = AdapterMessages(binding.recyclerViewMessages, 5);
        initializeAdapter()
        configureViews()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureServerEvents()
    }

    override fun onDestroy() {
        super.onDestroy()
        eventSource.close()
        Log.d(MessagesFragment::class.simpleName, "closing server side event")
    }

    private fun configureServerEvents() {
        val eventHandler: EventHandler = object : EventHandler {
            override fun onOpen() {
                Log.d(MessagesFragment::class.simpleName, "Seriver side event opened")
            }

            override fun onComment(comment: String?) {

            }

            override fun onMessage(event: String?, messageEvent: MessageEvent?) {
                Log.d(MessagesFragment::class.simpleName, messageEvent?.data.toString())
            }

            override fun onClosed() {

            }

            override fun onError(t: Throwable?) {

            }

        }
        val event: EventSource.Builder = EventSource.Builder(
                eventHandler,
                URI.create("http://www.andreiram.co.uk:3000/.well-known/mercure?topic=/chat")
        )
                .reconnectTime(Duration.ofMillis(10));
        eventSource = event.build();
        eventSource.start();
    }

    private fun configureViews() {
        binding.sendMessageButton.setOnClickListener {
            if (binding.messageTextArea.text != null) {
                val messageContent = binding.messageTextArea.text.toString().trim { it <= ' ' }
                if (messageContent.trim { it <= ' ' } != "") {
                    binding.messageTextArea.text!!.clear()
                }
            }
        }
    }

    private fun initializeAdapter() {
        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        binding.recyclerViewMessages.adapter = adapterMessages
        binding.recyclerViewMessages.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(requireContext())
    }
}