package com.example.dataLayer.dataMappers

import com.example.bookapp.models.UserMessage
import com.example.dataLayer.models.MessageDTO

object MessageMapper {

    fun mapNetworkToDomainObject(messageDTO: MessageDTO): UserMessage {
        var messageImage:String? = null
        if(messageDTO.messageImage !=null) {
           messageImage = ("http://sgb967.poseidon.salford.ac.uk/cms/" + messageDTO.messageImage)
        }
        return UserMessage(
                messageID = messageDTO.messageID,
                messageContent = messageDTO.messageContent,
                senderID = messageDTO.senderID,
                receiverID = messageDTO.receiverID,
                messageDate = messageDTO.messageDate,
                messageImage = messageImage
        )
    }

    fun mapNetworkToDomainObjects(messages: ArrayList<MessageDTO>): ArrayList<UserMessage> {
        val toReturn = ArrayList<UserMessage>()
        messages.forEach {
            toReturn.add(mapNetworkToDomainObject(it))
        }
        return toReturn;
    }
}