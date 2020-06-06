package com.example.bookapp

import com.example.dataLayer.interfaces.ChatInterface
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import retrofit2.Retrofit

class ChatRepositoryTests {
    private val chatInterface: ChatInterface = AppUtilities.getRetrofit()
            .create(ChatInterface::class.java)

    @Test
    fun shouldReturnHubLink() = runBlocking {
        val data = chatInterface.fetchChatLink(1)
        Assert.assertNotNull(data)
    }
}


