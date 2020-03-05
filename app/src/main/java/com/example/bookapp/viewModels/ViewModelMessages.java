package com.example.bookapp.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.api.MessageRepository;
import com.example.bookapp.models.Message;

import java.util.ArrayList;


public class ViewModelMessages extends ViewModel implements MessageRepository.MessageRepositoryCallback {
    //the last messages in the chat
    private MutableLiveData<ArrayList<Message>> messages = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Message>> currentlyFetchedNewMessages = new MutableLiveData<>();
    private MutableLiveData<Message> currentlySentMessage = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Message>> getMessages() {
        return messages;
    }

    public MutableLiveData<Message> getCurrentlySentMessage() {
        return currentlySentMessage;
    }

    public MutableLiveData<ArrayList<Message>> getCurrentlyFetchedNewMessages() {
        return currentlyFetchedNewMessages;
    }

    /*************************************************INTERFACE METHODS ************************************/

    @Override
    public void onOldMessagesFetched(@NonNull ArrayList<Message> oldMessages) {
        ArrayList<Message> newData = new ArrayList<>();
        if (this.messages.getValue() != null) {
            newData.addAll(this.messages.getValue());
        }
        for (Message message : oldMessages) {
            newData.add(0, message);
        }

        this.messages.setValue(newData);
    }


    @Override
    public void onNewMessagesReady(@NonNull ArrayList<Message> newMessages) {
        //create a new arrayList to trigger the observer
        if (this.messages.getValue() != null) {
            this.messages.getValue().addAll(newMessages);
        }
        currentlyFetchedNewMessages.setValue(newMessages);
    }

    @Override
    public void onSendMessageReady(@NonNull Message message) {
        currentlySentMessage.setValue(message);
    }


}
