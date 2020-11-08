package com.andrei.dataLayer.engineUtils

import android.util.Log
import retrofit2.HttpException
import java.net.SocketTimeoutException

open class ResponseHandler {

    private val TAG = ResponseHandler::class.java.simpleName

    fun <T : Any> handleSuccess(data: T): Resource<T> {
        return Resource.success(data)
    }

    fun <T : Any> handleException(e: Exception,url:String): Resource<T> {
        Log.e(TAG,"Error at $url with error ${e.stackTraceToString()}")

        return when (e) {
            is HttpException -> Resource.error(getErrorMessage(e.code()))
            is SocketTimeoutException -> Resource.error(getErrorMessage(0))
            else -> Resource.error(getErrorMessage(Int.MAX_VALUE))
        }
    }


    private fun getErrorMessage(code: Int): String {
        return when (code) {
            0 -> "Timeout"
            401 -> "Unauthorised"
            404 -> "Not found"
            else -> "Something went wrong"
        }
    }
}