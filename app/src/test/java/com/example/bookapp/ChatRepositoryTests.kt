package com.example.bookapp

import com.example.dataLayer.interfaces.ChatInterface
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class ChatRepositoryTests {
    private val chatInterface: ChatInterface = Retrofit.Builder().baseUrl("http://www.andreiram.co.uk/")
            .addConverterFactory(ScalarsConverterFactory.create()).build()
            .create(ChatInterface::class.java)

    @Test
    fun shouldReturnHubLink() = runBlocking {
        val data = chatInterface.fetchChatLink(1)
        Assert.assertNotNull(data)
    }
}


