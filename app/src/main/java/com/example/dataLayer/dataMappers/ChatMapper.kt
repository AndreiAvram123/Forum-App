package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Chat
import com.example.dataLayer.models.ChatDTO

object ChatMapper {
     fun mapDtoObjectToDomainObject(chatDTO: ChatDTO, userID: Int): Chat {
        // if (chatDTO.type == "OneToOne") {
        val firstUser = UserMapper.mapToDomainObject(chatDTO.users[0])
        val secondUser = UserMapper.mapToDomainObject(chatDTO.users[1])
        return if (firstUser.userID == userID) {
            Chat(chatID = chatDTO.id,
                    users = UserMapper.mapDTONetworkToDomainObjects(chatDTO.users),
                    image = secondUser.profilePicture,
                    name = secondUser.username
            )
        } else {
            Chat(chatID = chatDTO.id,
                    users = UserMapper.mapDTONetworkToDomainObjects(chatDTO.users),
                    image = firstUser.profilePicture,
                    name = firstUser.username)
        }
        // }
    }

    fun mapToDomainObjects(chatsDTO: List<ChatDTO>, userID: Int): List<Chat> = chatsDTO.map { mapDtoObjectToDomainObject(it, userID) }
}