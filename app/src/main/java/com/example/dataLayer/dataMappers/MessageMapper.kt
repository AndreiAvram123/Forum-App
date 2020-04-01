package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Message
import com.example.dataLayer.models.MessageDTO

object MessageMapper {

    fun mapNetworkToDomainObject(messageDTO: MessageDTO): Message {
        var messageImage:String? = null
        if(messageDTO.messageImage !=null) {
           messageImage = ("http://sgb967.poseidon.salford.ac.uk/cms/" + messageDTO.messageImage)
        }
        return Message(
                messageID = messageDTO.messageID,
                messageContent = messageDTO.messageContent,
                senderID = messageDTO.senderID,
                receiverID = messageDTO.receiverID,
                messageDate = messageDTO.messageDate,
                messageImage = messageImage
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