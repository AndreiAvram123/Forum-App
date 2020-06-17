package com.example.dataLayer.repositories

import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import com.example.bookapp.models.MessageDTO
import com.example.dataLayer.dataMappers.MessageMapper
import com.example.dataLayer.models.ChatNotificationDTO
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import org.json.JSONObject
import java.net.URI
import java.time.Duration
import javax.inject.Inject

class NotificationHandler(
        private val connectivityManager: ConnectivityManager,
        private val addNotification: (notification: ChatNotificationDTO) -> Unit,
        private val channelURL: String
) {

    private lateinit var eventSource: EventSource

    init {
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
            }
        })
        if (connectivityManager.activeNetwork != null) {
            configureServeSideEvents()
        }

    }

    private fun configureServeSideEvents() {


        val eventHandler: EventHandler = object : EventHandler {
            override fun onOpen() {}
            override fun onComment(comment: String?) {}
            override fun onClosed() {}
            override fun onError(t: Throwable?) {}


            override fun onMessage(event: String?, messageEvent: MessageEvent) {
                val gson: Gson = GsonBuilder().setPrettyPrinting().create()
                val data = messageEvent.data

                if (data != null) {
                    val jsonObject = JSONObject(data)
                    Log.d("haha", jsonObject.toString())
                }
            }
        }


        val event: EventSource.Builder = EventSource.Builder(
                eventHandler,
                URI.create(channelURL)
        )
                .reconnectTime(Duration.ofMillis(10));
        val temp = event.build()
        eventSource = temp
        temp.start()
    }
}
