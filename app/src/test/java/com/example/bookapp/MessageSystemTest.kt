package com.example.bookapp

import android.util.Log
import com.example.bookapp.fragments.MessagesFragment
import com.example.bookapp.models.MessageDTO
import com.example.dataLayer.interfaces.ChatInterface
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test
import java.net.URI
import java.time.Duration

class MessageSystemTest {
    val chatRepo = AppUtilities.getRetrofit().create(ChatInterface::class.java)

    @Test
    fun shouldReturnChatLink() = runBlocking {
        val link = chatRepo.fetchChatLink(3)
        Assert.assertNotNull(link)
    }

}