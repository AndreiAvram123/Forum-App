package com.andrew.dataLayer.engineUtils

import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.Exception

class AuthInterceptor(private val token: String,
                      private val connectivityManager: ConnectivityManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if(connectivityManager.activeNetwork == null){
            throw NoInternetException()
        }
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        requestBuilder.addHeader("Authorization", "Bearer $token")

        return chain.proceed(requestBuilder.build())
    }
}
class NoInternetException : Exception() {
    override val message: String?
        get() = "No internet present, cannot execute request..."
}