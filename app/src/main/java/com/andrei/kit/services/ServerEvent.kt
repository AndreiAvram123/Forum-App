package com.andrei.kit.services

import android.net.ConnectivityManager
import android.net.Network
import com.andrei.kit.utils.isConnected
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import java.net.URI
import java.time.Duration

class ServerEvent  {


    var onMessageListener:((data:String)->Unit)? = null
    var url:String? = null
    set(value) {
        field = value
        if(value!=null){
            buildEventSource()
        }
    }


    private var eventSource:EventSource? = null

    private fun buildEventSource() {
       eventSource = EventSource.Builder(
                 eventHandler,
                URI.create(url)
        ).reconnectTime(Duration.ofMillis(1000)).build()
    }


    fun start()= eventSource?.start()
    fun stop() = eventSource?.close()


    private val eventHandler: EventHandler = object:EventHandler{
        override fun onOpen() {
        }

        override fun onClosed() {
        }

        override fun onMessage(event: String?, messageEvent: MessageEvent) {
            messageEvent.data?.let {
                onMessageListener?.invoke(it)
            }
        }
        override fun onComment(comment: String?) {
        }

        override fun onError(t: Throwable?) {
        }

    }

    fun getNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
           start()
        }


        override fun onLost(network: Network) {
            stop()
        }
    }

}