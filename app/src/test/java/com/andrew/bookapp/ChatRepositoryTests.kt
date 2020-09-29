package com.andrew.bookapp

import com.andrew.TestUtilities
import com.andrew.dataLayer.interfaces.ChatRepositoryInterface
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


