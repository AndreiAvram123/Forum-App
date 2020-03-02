package com.example.bookapp.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.api.MessageRepository;
import com.example.bookapp.models.Message;

import java.util.ArrayList;

public class ViewModelMessages extends ViewModel implements MessageRepository.MessageRepositoryCallback {
    private MutableLiveData<ArrayList<Message>> lastMessages = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Message>> getLastMessages() {
        return lastMessages;
    }
    private MutableLiveData<Message> currentFetchedMessage = new MutableLiveData<>();

    public MutableLiveData<Message> getLastFetchedMessage() {
        return currentFetchedMessage;
    }

    @Override
    public void onLastMessagesReady(@NonNull ArrayList<Message> lastMessages) {
        this.lastMessages.setValue(lastMessages);
    }

    @Override
    public void onNewMessagesReady(@NonNull ArrayList<Message> messages) {
        //create a new arrayList to trigger the observer
        ArrayList<Message> newData = new ArrayList<>();
        if (lastMessages.getValue() != null) {
            newData.addAll(lastMessages.getValue());

        }
        newData.addAll(messages);
        lastMessages.setValue(newData);
    }

    @Override
    public void onNewMessageReady(@NonNull Message message) {
     currentFetchedMessage.setValue(message);
    }
}
