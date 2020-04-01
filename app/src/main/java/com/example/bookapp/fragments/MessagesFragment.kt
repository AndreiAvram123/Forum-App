package com.example.bookapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.Adapters.AdapterMessages
import com.example.bookapp.R
import com.example.bookapp.databinding.MessagesFragmentBinding
import com.example.bookapp.viewModels.ViewModelMessages
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MessagesFragment : Fragment() {
    private lateinit var binding: MessagesFragmentBinding;
    private val viewModelMessages: ViewModelMessages by activityViewModels()
    private val adapterMessages: AdapterMessages by lazy {
        AdapterMessages()
    }
    private lateinit var currentUserID: String
    private lateinit var user2ID: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.messages_fragment, container, false)
        currentUserID = MessagesFragmentArgs.fromBundle(requireArguments()).currentUserID
        user2ID = MessagesFragmentArgs.fromBundle(requireArguments()).user2ID
        attachObserver()
        initializeAdapter()
        configureViews()
        return binding.root
    }

    private fun configureViews() {
        binding.sendMessageButton.setOnClickListener { view: View? ->
            if (binding.messageTextArea.text != null) {
                val messageContent = binding.messageTextArea.text.toString().trim { it <= ' ' }
                if (messageContent.trim { it <= ' ' } != "") {
                    binding.messageTextArea.text!!.clear()

                }
            }
        }
    }

    private fun attachObserver() {
        viewModelMessages.getRecentMessages(currentUserID, user2ID).observe(viewLifecycleOwner,
                Observer {
                    adapterMessages.addOldMessages(it)
                })
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