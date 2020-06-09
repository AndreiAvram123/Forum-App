package com.example.bookapp

import com.example.dataLayer.interfaces.ChatRepositoryInterface
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class MessageSystemTest {
    val chatRepo = AppUtilities.getRetrofit().create(ChatRepositoryInterface::class.java)

    @Test
    fun shouldReturnChatLink() = runBlocking {
        val link = chatRepo.fetchChatLink(3)
        Assert.assertNotNull(link)
    }

}