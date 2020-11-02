package com.andrei.dataLayer.dataMappers

import com.andrei.kit.models.Chat
import com.andrei.dataLayer.models.ChatDTO

object ChatMapper {
    fun mapDtoObjectToDomainObject(chatDTO: ChatDTO, userID: String): Chat {
        val firstUser = UserMapper.mapToDomainObject(chatDTO.users[0])
        val secondUser = UserMapper.mapToDomainObject(chatDTO.users[1])
        return if (firstUser.userID == userID) {
            Chat(chatID = chatDTO.id,
                    user2 = secondUser,
                    image = secondUser.profilePicture,
                    name = secondUser.username
            )
        } else {
            Chat(chatID = chatDTO.id,
                    user2 = firstUser,
                    image = firstUser.profilePicture,
                    name = firstUser.username)
        }
    }

    fun mapToDomainObjects(chatsDTO: List<ChatDTO>, userID: String): List<Chat> = chatsDTO.map { mapDtoObjectToDomainObject(it, userID) }
}