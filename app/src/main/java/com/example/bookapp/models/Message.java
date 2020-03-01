package com.example.bookapp.models;

public class Message {
    private String messageContent;
    private long messageDate;
    private String senderID;

    public Message(String messageContent, long messageDateUnix, String senderID) {
        this.messageContent = messageContent;
        this.messageDate = messageDateUnix;
        this.senderID = senderID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public long getMessageDate() {
        return messageDate;
    }

    public String getSenderID() {
        return senderID;
    }
}
