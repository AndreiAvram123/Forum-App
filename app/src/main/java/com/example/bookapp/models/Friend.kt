package com.example.bookapp.models

class Friend(val lastMessage: Message,
             userID: String,
             username: String,
             email: String?
) : User(userID, username, email) {

}