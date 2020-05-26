package com.example.dataLayer.dataMappers

import com.example.bookapp.models.User
import com.example.dataLayer.models.UserDTO

object UserMapper {

    fun mapNetworkToDomainObject(userDTO: UserDTO) =User(userID = userDTO.userID, username = userDTO.username, email = userDTO.email)
}