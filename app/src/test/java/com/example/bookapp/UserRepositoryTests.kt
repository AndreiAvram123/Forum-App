package com.example.bookapp

import com.example.dataLayer.interfaces.UserRepositoryInterface
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UserRepositoryTests {

    val repo = AppUtilities.retrofitGsonConverter.create(UserRepositoryInterface::class.java)

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
}