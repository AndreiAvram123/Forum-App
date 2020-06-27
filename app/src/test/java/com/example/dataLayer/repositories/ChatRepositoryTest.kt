package com.example.dataLayer.repositories

import com.example.TestUtilities
import com.example.dataLayer.interfaces.ChatRepositoryInterface
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@InternalCoroutinesApi

class ChatRepositoryTest {

    private val repo = TestUtilities.retrofit.create(ChatRepositoryInterface::class.java)
    @Test
    fun shouldReturnRecentMessages() = runBlocking {
        val fetchedData = repo.fetchRecentMessages(12)
        assertNotNull(fetchedData)
    }
    @Test
    fun shouldMarkMessageAsSeen()= runBlocking {
        repo.markMessageAsSeen(109,1017)

    }
}