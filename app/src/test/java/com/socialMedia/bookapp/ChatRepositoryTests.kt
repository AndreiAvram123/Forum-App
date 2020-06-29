package com.socialMedia.bookapp

import com.socialMedia.TestUtilities
import com.socialMedia.dataLayer.interfaces.ChatRepositoryInterface
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class ChatRepositoryTests {
    private val chatRepositoryInterface: ChatRepositoryInterface = TestUtilities.retrofit
            .create(ChatRepositoryInterface::class.java)

    @Test
    fun shouldReturnHubLink() = runBlocking {
        val data = chatRepositoryInterface.fetchChatURL(1)
        Assert.assertNotNull(data)
    }
}


