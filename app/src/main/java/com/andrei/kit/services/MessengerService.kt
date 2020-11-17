package com.andrei.kit.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Handler
import android.os.IBinder
import android.os.Messenger
import com.andrei.dataLayer.LocalDatabase
import com.andrei.dataLayer.dataMappers.toMessage
import com.andrei.kit.dagger.MyApplication
import com.andrei.kit.models.MessageDTO
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

const val new_chat_link_message = 1
const val key_chats_link = "KEY_CHAT_LINKS"

class MessengerService : Service() {



    private lateinit var messenger: Messenger

    private val serverEvent by lazy {
        ServerEvent()
    }


    private lateinit var connectivityManager: ConnectivityManager



    private val messageDao by lazy {
        LocalDatabase.getDatabase(application as MyApplication).messageDao()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(serverEvent.getNetworkCallback())
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
                new_chat_link_message -> {
                    serverEvent.url = msg.data.getString(key_chats_link)
                    serverEvent.start()
                    serverEvent.onMessageListener = this@MessengerService::onNewMessage

                }
            }
        }
    }

    private fun onNewMessage(data:String){
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()

            val jsonObject = JSONObject(data)

            val messageDTO = gson.fromJson(jsonObject.toString(), MessageDTO::class.java)

            val message = messageDTO.toMessage()

            CoroutineScope(Dispatchers.IO).launch {
                messageDao.insertMessage(message)
            }

    }
    override fun onDestroy() {
        super.onDestroy()
        serverEvent.stop()
    }

}


