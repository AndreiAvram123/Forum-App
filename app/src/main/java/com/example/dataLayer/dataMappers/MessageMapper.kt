package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Message
import com.example.dataLayer.models.MessageDTO

object MessageMapper {

    fun mapNetworkToDomainObject(message: MessageDTO): Message {
        val builder: Message.Builder = Message.Builder()
        builder.setMessageID(message.messageID)
        builder.setMessageContent(message.messageContent)
        builder.setSenderID(message.senderID)
        builder.setMessageDate(message.messageDate)
        return builder.createMessage()


    }
}