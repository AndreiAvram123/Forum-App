package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Message
import com.example.bookapp.models.MessageDTO

object MessageMapper {

    fun mapToDomainObject(messageDTO: MessageDTO) = Message(id = messageDTO.id,
            content = messageDTO.content, sender = UserMapper.mapToDomainObject(messageDTO.sender),
            type = messageDTO.type, seenByCurrentUser = messageDTO.seenByUser, localID = messageDTO.localID, chatID = messageDTO.chatID, date = messageDTO.date)
}