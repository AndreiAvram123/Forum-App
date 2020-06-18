package com.example.dataLayer.repositories

import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend

@Singleton
class RequestExecutor @Inject constructor(
        private val connectivityManager: ConnectivityManager,
        private val coroutineScope: CoroutineScope
) {


    inner class NetworkRequest(val function: KFunction<Any>, val parameter: Any?)

    private val uncompletedNetworkRequests: Queue<NetworkRequest> = LinkedList()
    private val onNetworkAvailableQueue: Queue<NetworkRequest> = LinkedList()

    init {
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                coroutineScope.launch {
                    while (uncompletedNetworkRequests.peek() != null) {
                        executeRequest(uncompletedNetworkRequests.poll())
                    }
                    onNetworkAvailableQueue.iterator().forEachRemaining { executeRequest(it) }

                }
            }

        })

    }


    internal fun add(function: KFunction<Any>, parameter: Any?) {

        val request = NetworkRequest(function, parameter)
        if (connectivityManager.activeNetwork != null) {
            executeRequest(request)
        } else {
            uncompletedNetworkRequests.add(request)
        }
    }

    internal fun addOnNetworkAvailable(function: KFunction<Any>, parameter: Any?) =
            onNetworkAvailableQueue.add(NetworkRequest(function, parameter))


    private fun executeRequest(networkRequest: NetworkRequest) {
        coroutineScope.launch {
            val param = networkRequest.parameter
            if (param != null) {
                networkRequest.function.callSuspend(param)
            } else {
                networkRequest.function.callSuspend()
            }
        }
    }
}