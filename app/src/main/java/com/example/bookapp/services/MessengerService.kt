package com.example.bookapp.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Messenger
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.bookapp.R
import com.example.bookapp.dagger.MyApplication
import com.example.bookapp.getConnectivityManager
import com.example.bookapp.models.Message
import com.example.bookapp.models.MessageDTO
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.dataMappers.MessageMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.internal.closeQuietly
import org.json.JSONObject
import java.net.URI
import java.time.Duration

const val new_chat_link_message = 1
const val key_chats_link = "KEY_CHAT_LINKS"
const val new_user_id_message = 2
const val play_notification_message = 3
const val stop_notification_message = 4

@InternalCoroutinesApi
class MessengerService : Service() {


    private var eventSource: EventSource? = null

    private lateinit var messenger: Messenger
    private var userID: Int = 0

    var shouldPlayNotification = false

    private val messageDao by lazy {
        PostDatabase.getDatabase(application as MyApplication).messageDao()
    }

    private var chatLinks: String? = null
        set(value) {
            field = value
            startServerSideEvent()
        }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY


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
                new_chat_link_message -> {
                    chatLinks = msg.data.getString(key_chats_link)
                }
                new_user_id_message -> {
                    userID = msg.arg1
                }
                play_notification_message -> shouldPlayNotification = true

                stop_notification_message -> shouldPlayNotification = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        eventSource?.closeQuietly()

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

                when (jsonObject.get("type")) {
                    "message" -> {
                        val messageDTO = gson.fromJson(jsonObject.get("message").toString(), MessageDTO::class.java)

                        val message = MessageMapper.mapToDomainObject(messageDTO)
                        if (message.sender.userID == userID) {
                            message.seenByCurrentUser = true
                        }
                        messageDao.insertMessageCurrentThread(message)

                        if (shouldPlayNotification) {
                            playNotification(message)
                        }
                    }

                }
            }
        }
    }


    private fun startServerSideEvent() {
        chatLinks?.let {
            val event: EventSource.Builder = EventSource.Builder(
                    getEventHandler(),
                    URI.create(it)
            ).reconnectTime(Duration.ofMillis(10))
            eventSource = event.build()
            startEvent()
        }
    }

    private fun startEvent() {
        if (applicationContext.getConnectivityManager().activeNetwork != null) {
            eventSource?.start()
        }
    }


    private fun playNotification(message: Message) {
        val builder = NotificationCompat.Builder(this@MessengerService, getString(R.string.message_channel_id))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("New message")
                .setContentText(message.content.take(10) + "...")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        //todo
        //shuld open activity
//        pendingIntent?.let {
//            builder.setContentIntent(it)
//        }

        with(NotificationManagerCompat.from(this@MessengerService)) {
            notify(message.id, builder.build())
        }
    }

}

