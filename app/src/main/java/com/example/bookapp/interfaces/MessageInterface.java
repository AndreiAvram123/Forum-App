package com.example.bookapp.interfaces;

import androidx.annotation.NonNull;

import com.example.bookapp.models.Message;

public interface MessageInterface {
    void sendMessage(@NonNull Message message,@NonNull String user2ID);
}
