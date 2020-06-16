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

    init {
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                coroutineScope.launch {
                    //execute requests that are not finished
                    while (uncompletedNetworkRequests.peek() != null) {
                        val request = uncompletedNetworkRequests.poll()
                        val param = request.parameter
                        if (param != null) {
                            request.function.callSuspend(param)
                        } else {
                            request.function.callSuspend()
                        }
                    }
                }
            }

            override fun onLost(network: Network?) {
                //take action when network connection is lost
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