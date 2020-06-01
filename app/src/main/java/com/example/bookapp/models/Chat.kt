package com.example.bookapp.models

data class Chat(
        val chatID: Int,
        val users: List<User>,
        val name: String,
        val image: String
)