package com.example.bookapp.models;

public class Message {
    private String messageID;
    private String messageContent;
    private long messageDate;
    private String senderID;

    public Message(String messageID, String messageContent, long messageDate, String senderID) {
        this.messageID = messageID;
        this.messageContent = messageContent;
        this.messageDate = messageDate;
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

    public static class Builder {

        private String messageID;
        private String messageContent;
        private long messageDate;
        private String senderID;

        public Builder setMessageID(String messageID) {
            this.messageID = messageID;
            return this;
        }

        public Builder setMessageContent(String messageContent) {
            this.messageContent = messageContent;
            return this;
        }

        public Builder setMessageDate(long messageDate) {
            this.messageDate = messageDate;
            return this;
        }

        public Builder setSenderID(String senderID) {
            this.senderID = senderID;
            return this;
        }

        public Message createMessage() {
            if (messageID == null) {
                throw new IllegalStateException("Message id cannot be null");
            }
            if (messageDate <= 0) {
                throw new IllegalStateException("Message date cannot be 0");
            }
            return new Message(messageID, messageContent, messageDate, senderID);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        if (messageDate != message.messageDate) return false;
        if (!messageID.equals(message.messageID)) return false;
        if (!messageContent.equals(message.messageContent)) return false;
        return senderID.equals(message.senderID);
    }

    @Override
    public int hashCode() {
        int result = messageID.hashCode();
        result = 31 * result + messageContent.hashCode();
        result = 31 * result + (int) (messageDate ^ (messageDate >>> 32));
        result = 31 * result + senderID.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageID='" + messageID + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", messageDate=" + messageDate +
                ", senderID='" + senderID + '\'' +
                '}';
    }
}