package com.example.dataLayer.dataMappers

import com.example.bookapp.models.User
import com.example.dataLayer.models.UserDTO


//todo
//use extension functions
fun UserDTO.toUser()  = User(userID = this.userID, username = this.username, email = this.email, profilePicture = this.profilePicture)


object UserMapper {


    fun mapDomainToNetworkObject(user:User)  = UserDTO(userID = user.userID,username = user.username,email = user.email,profilePicture = user.profilePicture)
}