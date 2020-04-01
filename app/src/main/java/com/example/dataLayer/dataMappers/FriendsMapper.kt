package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Friend
import com.example.dataLayer.models.FriendDTO

object FriendsMapper {

    fun mapNetworkToDomainObjects(friendsDTO: ArrayList<FriendDTO>): ArrayList<Friend> {
        val toReturn: ArrayList<Friend> = ArrayList()
        friendsDTO.forEach {
            toReturn.add(mapNetworkToDomainObject(it))
        }
        return toReturn
    }

    private fun mapNetworkToDomainObject(friendDTO: FriendDTO): Friend {
        return Friend(
                lastMessage = MessageMapper.mapNetworkToDomainObject(friendDTO.lastMessage)
                , userID = friendDTO.userID
                , username = friendDTO.username
                , email = null)
    }
}
