package com.example.bookapp.models;

import java.lang.reflect.Field;

public class Friend extends User {
    private Message lastMessage;

    public static class Builder extends User.Builder {
        private Message lastMessage;

        public Builder setLastMessage(Message lastMessage) {
            this.lastMessage = lastMessage;
            return this;
        }

        @Override
        public Friend create() {
            Friend friend = new Friend();
            User user = super.create();
            friend.userID = user.getUserID();
            friend.email = user.getEmail();
            friend.profilePictureURL = user.getProfilePictureURL();
            friend.username = user.getUsername();
            friend.lastMessage = lastMessage;

            return friend;
        }
    }

    public Message getLastMessage() {
        return lastMessage;
    }
}
