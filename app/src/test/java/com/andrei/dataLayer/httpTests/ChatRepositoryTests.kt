package com.andrei.dataLayer.httpTests

import android.os.Build
import com.andrei.TestUtilities
import com.andrei.TestUtilities.Companion.testUserID
import com.andrei.TestUtilities.Companion.testUserID2
import com.andrei.dataLayer.interfaces.ChatRepositoryInterface
import com.andrei.dataLayer.models.serialization.SerializeFriendRequest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.await

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class ChatRepositoryTest {

    private lateinit var repo: ChatRepositoryInterface

    @Before
    fun setUp() {
        repo = TestUtilities.retrofit.create(ChatRepositoryInterface::class.java)
    }

    @Test
    fun shouldReturnHubLink() = runBlocking {
        val data = repo.fetchChatURL(testUserID).await()
        Assert.assertNotNull(data)
    }

    @Test
    fun shouldReturnFriendRequests() = runBlocking {
        val fetchedData = repo.fetchReceivedFriendRequests(testUserID)
        Assert.assertNotNull(fetchedData)
    }

    @Test
    fun `should return 200 response when fetching friend requests`() {
        runBlocking {
          repo.fetchReceivedFriendRequests(testUserID)
        }
    }



}