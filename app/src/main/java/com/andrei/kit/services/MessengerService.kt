package com.andrei.kit.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Handler
import android.os.IBinder
import android.os.Messenger
import com.andrei.kit.dagger.MyApplication
import com.andrei.kit.models.MessageDTO
import com.andrei.dataLayer.LocalDatabase
import com.andrei.dataLayer.dataMappers.toMessage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URI
import java.time.Duration

const val new_chat_link_message = 1
const val key_chats_link = "KEY_CHAT_LINKS"
const val key_user_id = "KEY_USER_ID"
const val new_user_id_message = 2
const val play_notification_message = 3
const val stop_notification_message = 4

class MessengerService : Service() {


    private var eventSource: EventSource? = null

    private lateinit var messenger: Messenger


    var shouldPlayNotification = false

    private var userID :String? = null

    private lateinit var connectivityManager: ConnectivityManager


    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            startServerSideEvent()
        }


        override fun onLost(network: Network) {
            eventSource?.close()
        }
    }


    private val messageDao by lazy {
        LocalDatabase.getDatabase(application as MyApplication).messageDao()
    }

    private var chatLinks: String? = null
        set(value) {
            field = value
            startServerSideEvent()
        }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        return START_STICKY
    }


    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    override fun onBind(intent: Intent): IBinder? {
        messenger = Messenger(ServiceHandler())
        return messenger.binder
    }


    inner class ServiceHandler : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            when (msg.what) {
                new_chat_link_message -> chatLinks = msg.data.getString(key_chats_link)
                new_user_id_message -> userID = msg.data.getString(key_user_id)
                play_notification_message -> shouldPlayNotification = true
                stop_notification_message -> shouldPlayNotification = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        eventSource?.close()

    }

    private fun getEventHandler(): EventHandler = object : EventHandler {
        override fun onOpen() {
        }

        override fun onComment(comment: String?) {}
        override fun onClosed() {}
        override fun onError(t: Throwable?) {}


        override fun onMessage(event: String?, messageEvent: MessageEvent) {
            val gson: Gson = GsonBuilder().setPrettyPrinting().create()
            val data = messageEvent.data

            if (data != null) {
                val jsonObject = JSONObject(data)

                 val messageDTO = gson.fromJson(jsonObject.toString(), MessageDTO::class.java)

                        val message = messageDTO.toMessage()

                        CoroutineScope(Dispatchers.IO).launch {
                            messageDao.insertMessage(message)
                        }

                }
            }
        }


    private fun startServerSideEvent() {
        chatLinks?.let {
            val event: EventSource.Builder = EventSource.Builder(
                    getEventHandler(),
                    URI.create(it)
            ).reconnectTime(Duration.ofMillis(100))
            eventSource = event.build()
            startEvent()
        }
    }

    private fun startEvent() {
        if (connectivityManager.activeNetwork != null) {
            eventSource?.start()
        }
    }

}


