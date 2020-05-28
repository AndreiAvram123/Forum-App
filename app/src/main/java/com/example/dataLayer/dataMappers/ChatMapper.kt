package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Chat
import com.example.dataLayer.models.ChatDTO

object ChatMapper {
    fun mapDtoObjectToDomainObject(chatDTO: ChatDTO) = Chat(chatID = chatDTO.id,
            users = UserMapper.mapDTONetworkToDomainObjects(chatDTO.users))

    fun mapDTOObjectsToDomainObjects(chatsDTO: List<ChatDTO>): List<Chat> = chatsDTO.map { mapDtoObjectToDomainObject(it) }
}