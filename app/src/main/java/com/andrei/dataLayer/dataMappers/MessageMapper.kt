package com.andrei.dataLayer.dataMappers

import com.andrei.bookapp.models.Message
import com.andrei.bookapp.models.MessageDTO

fun MessageDTO.toMessage() =
        Message(id = this.id,
                content = this.content, sender = UserMapper.mapToDomainObject(this.sender),
                type = this.type, seenByCurrentUser = this.seenByUser, localID = this.localID, chatID = this.chatID, date = this.date)
