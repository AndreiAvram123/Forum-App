package com.example.bookapp.models;

public class User {

    protected String userID;
    protected String username;
    protected String email;
    protected String profilePictureURL;

    public static class Builder {
        private String userID;
        private String username;
        private String email;
        private String imageURL;

        public Builder setUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }
        public Builder setImageURL(String imageURL){
            this.imageURL = imageURL;
            return this;
        }
        public Builder setEmail(String email){
            this.email = email;
            return this;
        }

        public User create(){
            if(userID == null){
                throw new IllegalStateException("User id cannot be null");
            }
            if(username == null){
                throw new IllegalStateException("Username cannot be null");
            }
            User user = new User();
            user.userID = userID;
            user.username = username;
            user.email = email;
            user.profilePictureURL = imageURL;
            return user;
        }
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }
}
