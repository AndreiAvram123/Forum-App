package com.example.bookapp.models;

public class Message {
    private String messageContent;
    private String messageDate;
    private String senderID;

    public Message(String messageContent, String messageDate, String senderID) {
        this.messageContent = messageContent;
        this.messageDate = messageDate;
        this.senderID = senderID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public String getSenderID() {
        return senderID;
    }
}
