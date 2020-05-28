package com.example.bookapp.fragments

import android.os.Bundle
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
import com.example.bookapp.interfaces.MessageInterface
import com.example.bookapp.viewModels.ViewModelMessages

class MessagesFragment : Fragment() {
    private lateinit var binding: MessagesFragmentBinding
    private val viewModelMessages: ViewModelMessages? = null
    private var adapterMessages: AdapterMessages? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.messages_fragment, container, false)
        initializeAdapter()
        configureViews()
        return binding.root
    }

    private fun configureViews() {
        binding!!.sendMessageButton.setOnClickListener { view: View? ->
            if (binding!!.messageTextArea.text != null) {
                val messageContent = binding!!.messageTextArea.text.toString().trim { it <= ' ' }
                if (messageContent.trim { it <= ' ' } != "") {
                    binding!!.messageTextArea.text!!.clear()
                }
            }
        }
    }

    private fun initializeAdapter() {
        if (adapterMessages == null) {
            adapterMessages = AdapterMessages(binding!!.recyclerViewMessages, 0)
            adapterMessages!!.setCallback(requireActivity() as MessageInterface)
        }
        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        binding!!.recyclerViewMessages.adapter = adapterMessages
        binding!!.recyclerViewMessages.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
        binding!!.recyclerViewMessages.layoutManager = LinearLayoutManager(requireContext())
    }
}