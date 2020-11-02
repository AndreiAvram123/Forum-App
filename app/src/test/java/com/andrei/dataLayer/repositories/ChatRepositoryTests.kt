package com.andrew.dataLayer.repositories

import com.andrew.TestUtilities
import com.andrew.TestUtilities.Companion.testUserID
import com.andrew.TestUtilities.Companion.testUserID2
import com.andrew.dataLayer.interfaces.ChatRepositoryInterface
import com.andrew.dataLayer.models.deserialization.FriendRequest
import com.andrew.dataLayer.models.serialization.SerializeFriendRequest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ChatRepositoryTest {

    private lateinit var repo: ChatRepositoryInterface

    @Before
    fun setUp() {
        repo = TestUtilities.retrofit.create(ChatRepositoryInterface::class.java)
    }

    @Test
    fun shouldReturnHubLink() = runBlocking {
        val data = repo.fetchChatURL(testUserID)
        Assert.assertNotNull(data)
    }

    @Test
    fun shouldUploadComment() {
        runBlocking {
            val friendRequest = SerializeFriendRequest(senderID = testUserID, receiverID = testUserID2)
            repo.sendFriendRequest(friendRequest)
        }
    }

    @Test
    fun shouldReturnFriendRequests() = runBlocking {
        val fetchedData = repo.fetchFriendRequests(testUserID)
        Assert.assertNotNull(fetchedData)
    }

    @Test
    fun `send friend request should return valid response`() {
        runBlocking {
            val receiverID = testUserID
            val senderID = testUserID2
            //create a friend request
            //and push it
            val friendRequest = SerializeFriendRequest(senderID = senderID, receiverID = receiverID)
            val response = repo.sendFriendRequest(friendRequest)
            Assert.assertTrue(response.successful)
        }
    }

    @Test
    fun `should return 200 response when fetching friend requests`() {
        runBlocking {
            val response = repo.fetchFriendRequests(testUserID)

        }
    }

    @Test
    fun `should fetch friends request return not empty friends array`() {
        runBlocking {
            val fetchedFriends = repo.fetchFriends(testUserID)
            assert(fetchedFriends.isNotEmpty())
        }
    }


}