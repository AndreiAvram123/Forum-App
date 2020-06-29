package com.socialMedia.dataLayer.dataMappers

import com.socialMedia.bookapp.models.User
import com.socialMedia.dataLayer.models.UserDTO
import com.google.firebase.auth.FirebaseUser


fun UserDTO.toUser() = User(userID = this.userID, username = this.username, email = this.email, profilePicture = this.profilePicture)

fun User.toDomainObject() = UserDTO(userID = this.userID, username = this.username, email = this.email, profilePicture = this.profilePicture)

//todo
//change  the id to be string
fun FirebaseUser.toUser():User {
    var username = displayName
    if (username == null) {
        username = "Unknown"
    }
    var email = email
    if (email == null) {
        email = "Unknown"
    }
    var photoURL = "https://robohash.org/an?set=set4"
    photoUrl?.let { photoURL = it.toString() }


    return User(
            userID = 0,
            username = username,
            email = email,
            profilePicture = photoURL

    )
}