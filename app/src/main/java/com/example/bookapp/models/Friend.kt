package com.example.bookapp.models

class Friend(val lastUserMessage: UserMessage,
             userID: String,
             username: String,
             email: String?
) : User(userID, username, email) {

}