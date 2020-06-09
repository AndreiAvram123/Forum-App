package com.example.bookapp

import com.example.dataLayer.interfaces.ChatRepositoryInterface
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class ChatRepositoryTests {
    private val chatRepositoryInterface: ChatRepositoryInterface = AppUtilities.getRetrofit()
            .create(ChatRepositoryInterface::class.java)

    @Test
    fun shouldReturnHubLink() = runBlocking {
        val data = chatRepositoryInterface.fetchChatLink(1)
        Assert.assertNotNull(data)
    }
}


