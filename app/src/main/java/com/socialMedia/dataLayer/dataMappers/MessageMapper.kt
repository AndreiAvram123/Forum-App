package com.socialMedia.dataLayer.dataMappers

import com.socialMedia.bookapp.models.Message
import com.socialMedia.bookapp.models.MessageDTO
//todo
//change this
fun MessageDTO.toMessage() =
        Message(id = this.id,
                content = this.content, sender = this.sender.toUser(""),
                type = this.type, seenByCurrentUser = this.seenByUser, localID = this.localID, chatID = this.chatID, date = this.date)
