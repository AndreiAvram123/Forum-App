package com.andrei.dataLayer.dataMappers

import com.andrei.bookapp.models.User
import com.andrei.dataLayer.models.UserDTO
import com.google.firebase.auth.FirebaseUser

object UserMapper {

    fun mapToDomainObject(userDTO: UserDTO) = User(userID = userDTO.userID, username = userDTO.username, email = userDTO.email, profilePicture = userDTO.profilePicture)
}
fun User.toNetworkObject() =  UserDTO(userID = this.userID,username = this.username,email = this.email,profilePicture = this.profilePicture)
fun UserDTO.toUser(): User = User(userID = this.userID, username = this.username, email = this.email, profilePicture = this.profilePicture)
fun FirebaseUser.toUser():User  = User(userID = this.uid,username = this.displayName!!,email = this.email!!, profilePicture = this.photoUrl.toString())