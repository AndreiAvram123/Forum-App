package com.example.bookapp.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.models.MessageDTO;
import com.example.dataLayer.repositories.MessageRepository;

import java.util.ArrayList;


public class ViewModelMessages extends ViewModel implements MessageRepository.MessageRepositoryCallback {
    //the last messages in the chat
    private MutableLiveData<ArrayList<MessageDTO>> messages = new MutableLiveData<>();
    private MutableLiveData<ArrayList<MessageDTO>> currentlyFetchedNewMessages = new MutableLiveData<>();
    private MutableLiveData<MessageDTO> currentlySentMessage = new MutableLiveData<>();

    public MutableLiveData<ArrayList<MessageDTO>> getMessages() {
        return messages;
    }

    public MutableLiveData<MessageDTO> getCurrentlySentMessage() {
        return currentlySentMessage;
    }

    public MutableLiveData<ArrayList<MessageDTO>> getCurrentlyFetchedNewMessages() {
        return currentlyFetchedNewMessages;
    }

    /*************************************************INTERFACE METHODS ************************************/

    @Override
    public void onOldMessagesFetched(@NonNull ArrayList<MessageDTO> oldMessageDTOS) {
        ArrayList<MessageDTO> newData = new ArrayList<>();
        if (this.messages.getValue() != null) {
            newData.addAll(this.messages.getValue());
        }
        newData.addAll(oldMessageDTOS);

        this.messages.setValue(newData);
    }


    @Override
    public void onNewMessagesReady(@NonNull ArrayList<MessageDTO> newMessageDTOS) {
        //create a new arrayList to trigger the observer
        if (this.messages.getValue() != null) {
            this.messages.getValue().addAll(newMessageDTOS);
        }
        currentlyFetchedNewMessages.setValue(newMessageDTOS);
    }

    @Override
    public void onSendMessageReady(@NonNull MessageDTO messageDTO) {
        currentlySentMessage.setValue(messageDTO);
    }


}
