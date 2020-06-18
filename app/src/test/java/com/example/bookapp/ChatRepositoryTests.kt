package com.example.bookapp

import com.example.TestUtilities
import com.example.dataLayer.interfaces.ChatRepositoryInterface
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


