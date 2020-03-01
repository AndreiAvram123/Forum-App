package com.example.bookapp.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.models.Message;

import java.util.ArrayList;

public class MessagesViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Message>> messages = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Message>> getMessages() {
        return messages;
    }
}
