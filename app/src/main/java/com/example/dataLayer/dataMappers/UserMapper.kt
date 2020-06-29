package com.example.dataLayer.dataMappers

import com.example.bookapp.models.User
import com.example.dataLayer.models.UserDTO


fun UserDTO.toUser() = User(userID = this.userID, username = this.username, email = this.email, profilePicture = this.profilePicture)

fun User.toDomainObject() = UserDTO(userID = this.userID, username = this.username, email = this.email, profilePicture = this.profilePicture)
