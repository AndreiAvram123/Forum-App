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

    @Override
    public void onLastMessagesReady(@NonNull ArrayList<Message> lastMessages) {
        this.lastMessages.setValue(lastMessages);
    }
}
