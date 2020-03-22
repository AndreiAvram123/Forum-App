package com.example.bookapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookapp.Adapters.AdapterMessages;
import com.example.bookapp.R;
import com.example.bookapp.databinding.MessagesFragmentBinding;
import com.example.bookapp.interfaces.MessageInterface;
import com.example.bookapp.viewModels.ViewModelMessages;

public class MessagesFragment extends Fragment {
    private MessagesFragmentBinding binding;
    private ViewModelMessages viewModelMessages;
    private AdapterMessages adapterMessages;
    private MessageInterface messageInterface;
    private String currentUserID;
    private String user2ID;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.messages_fragment, container, false);
        messageInterface = (MessageInterface) requireActivity();
        currentUserID = MessagesFragmentArgs.fromBundle(getArguments()).getCurrentUserID();
        user2ID = MessagesFragmentArgs.fromBundle(getArguments()).getUser2ID();
        if (viewModelMessages == null) {
            attachObserver();
        }
        initializeAdapter();
        configureViews();
        return binding.getRoot();
    }

    private void configureViews() {
        binding.sendMessageButton.setOnClickListener(view -> {
            if (binding.messageTextArea.getText() != null) {
                String messageContent = binding.messageTextArea.getText().toString().trim();
                if (!messageContent.trim().equals("")) {
                    binding.messageTextArea.getText().clear();
                    messageInterface.sendMessage(messageContent, user2ID, currentUserID);
                }
            }
        });

    }

    private void attachObserver() {
        viewModelMessages = new ViewModelProvider(requireActivity()).get(ViewModelMessages.class);
        viewModelMessages.getMessages().observe(getViewLifecycleOwner(), oldMessages -> {
            if (oldMessages != null) {
                adapterMessages.addOldMessages(oldMessages);
            }
        });
        viewModelMessages.getCurrentlySentMessage().observe(getViewLifecycleOwner(), lastFetchedMessage -> {
            adapterMessages.addMessage(lastFetchedMessage);
        });
        viewModelMessages.getCurrentlyFetchedNewMessages().observe(getViewLifecycleOwner(),newFetchedMessages->{
            adapterMessages.addNewMessages(newFetchedMessages);
        });

    }


    private void initializeAdapter() {
        if (adapterMessages == null) {
            adapterMessages = new AdapterMessages(binding.recyclerViewMessages, user2ID);
            adapterMessages.setCallback((MessageInterface) requireActivity());
        }
        configureRecyclerView();
    }

    private void configureRecyclerView() {
        binding.recyclerViewMessages.setAdapter(adapterMessages);
        binding.recyclerViewMessages.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL));
        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(requireContext()));

    }


}
