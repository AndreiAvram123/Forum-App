package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Message
import com.example.dataLayer.models.MessageDTO

object MessageMapper {

    fun mapNetworkToDomainObject(messageDTO: MessageDTO): Message {
        return Message(
                messageID = messageDTO.messageID,
                messageContent = messageDTO.messageContent,
                senderID = messageDTO.senderID,
                receiverID = messageDTO.receiverID,
                messageDate = messageDTO.messageDate,
                messageImage = messageDTO.messageImage
        )

    }

    fun mapNetworkToDomainObjects(messages: ArrayList<MessageDTO>): ArrayList<Message> {
        val toReturn = ArrayList<Message>()
        messages.forEach {
            toReturn.add(mapNetworkToDomainObject(it))
        }
        return toReturn;
    }
}