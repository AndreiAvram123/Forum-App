package com.example.dataLayer.repositories

import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend

class RequestExecutor @Inject constructor(
        private val connectivityManager: ConnectivityManager,
        private val coroutineScope: CoroutineScope
) {


    private val uncompletedNetworkRequests: Queue<DeferredFunction> = LinkedList()

    init {
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                coroutineScope.launch {
                    //execute requests that are not finished
                    while (uncompletedNetworkRequests.peek() != null) {
                        uncompletedNetworkRequests.poll().function.callSuspend()
                    }
                }
            }

            override fun onLost(network: Network?) {
                //take action when network connection is lost
            }
        })

    }


    class DeferredFunction(val function: KFunction<Any>) {
        @Suppress("UNCHECKED_CAST")
        operator fun <T> invoke(): T {
            return function.call() as T
        }
    }


    internal fun executeSuspend(function: DeferredFunction) {
        if (connectivityManager.activeNetwork != null) {
            coroutineScope.launch {
                function.function.callSuspend()
            }
        } else {
            uncompletedNetworkRequests.add(function)
        }
    }

}