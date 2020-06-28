package com.example.dataLayer.dataMappers

import com.example.bookapp.models.User
import com.example.dataLayer.models.UserDTO


//todo
//use extension functions
object UserMapper {

    fun mapToDomainObject(userDTO: UserDTO) = User(userID = userDTO.userID, username = userDTO.username, email = userDTO.email, profilePicture = userDTO.profilePicture)


    fun mapDomainToNetworkObject(user:User)  = UserDTO(userID = user.userID,username = user.username,email = user.email,profilePicture = user.profilePicture,token = null)
}