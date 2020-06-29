package com.socialMedia.dataLayer.dataMappers

import com.socialMedia.bookapp.models.Chat
import com.socialMedia.bookapp.models.User
import com.socialMedia.dataLayer.models.ChatDTO

object ChatMapper {
    fun mapDtoObjectToDomainObject(chatDTO: ChatDTO, userID: Int): Chat {
//        val firstUser = chatDTO.users[0].toUser()
//        val secondUser = chatDTO.users[1].toUser()
//        return if (firstUser.userID == userID) {
//            Chat(chatID = chatDTO.id,
//                    user2 = secondUser,
//                    image = secondUser.profilePicture,
//                    name = secondUser.username
//            )
//        } else {
//            Chat(chatID = chatDTO.id,
//                    user2 = firstUser,
//                    image = firstUser.profilePicture,
//                    name = firstUser.username)
//        }
        return Chat(chatID = 0,name = "",image = "",user2 = User(userID = "",username = "",email = "",profilePicture = ""))
    }

    fun mapToDomainObjects(chatsDTO: List<ChatDTO>, userID: Int): List<Chat> = chatsDTO.map { mapDtoObjectToDomainObject(it, userID) }
}