package com.example.bookapp.models

open class User(
        val userID: String,
        val username: String,
        val email: String?

) {
 val profilePictureURl = "https://robohash.org/$username?size=100x100&set=set4"
}