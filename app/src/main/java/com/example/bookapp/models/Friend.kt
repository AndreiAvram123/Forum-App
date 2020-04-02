package com.example.bookapp.models

class Friend(val lastMessage: UserMessage,
             userID: String,
             username: String,
             email: String?
) : User(userID, username, email) {

}