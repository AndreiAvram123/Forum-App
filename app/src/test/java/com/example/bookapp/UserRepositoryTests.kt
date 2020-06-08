package com.example.bookapp

import com.example.dataLayer.interfaces.UserRepositoryInterface
import com.example.dataLayer.models.deserialization.DeserializeFriendRequest
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UserRepositoryTests {

    val repo = AppUtilities.getRetrofit().create(UserRepositoryInterface::class.java)

    @Test
    fun shouldUploadComment() = runBlocking {
        val friendRequest = SerializeFriendRequest(senderID = 109, receiverID = 108)
        repo.pushFriendRequest(friendRequest)
    }

    @Test
    fun shouldReturnFriendRequests() = runBlocking {
        val fetchedData = repo.fetchFriendRequests(109)
        Assert.assertNotNull(fetchedData)
    }

    @Test
    fun usersShouldBeFriendIfRequestIsAccepted() = runBlocking {
        val receiverID = 27
        val senderID = 28
        //create a friend request
        //and push it
        val friendRequest = SerializeFriendRequest(senderID = senderID, receiverID = receiverID)
        repo.pushFriendRequest(friendRequest)

        //fetch the friend requests and get the id of the last
        val requests: ArrayList<DeserializeFriendRequest> = ArrayList(repo.fetchFriendRequests(receiverID))


        //accept the friend request
        repo.acceptFriendRequest(requests.last().id)

        //get the receiver friends and check if the sender is now part of the friends
        val fetchedFriends = repo.fetchFriends(receiverID)
        val friend = fetchedFriends.find { it.userID == senderID }

        Assert.assertNotNull(friend)

        repo.removeFriend(userID = receiverID, friendID = senderID)

    }
}