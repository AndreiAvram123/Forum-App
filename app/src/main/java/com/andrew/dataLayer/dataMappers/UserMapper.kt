package com.andrew.dataLayer.dataMappers

import com.andrew.bookapp.models.User
import com.andrew.dataLayer.models.UserDTO

object UserMapper {

    fun mapToDomainObject(userDTO: UserDTO) = User(userID = userDTO.userID, username = userDTO.username, email = userDTO.email, profilePicture = userDTO.profilePicture)


    fun mapDomainToNetworkObject(user:User)  = UserDTO(userID = user.userID,username = user.username,email = user.email,profilePicture = user.profilePicture,token = null)
}