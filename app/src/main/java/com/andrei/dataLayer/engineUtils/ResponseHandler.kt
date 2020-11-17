package com.andrei.dataLayer.engineUtils

import android.util.Log
import com.andrei.kit.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import retrofit2.HttpException
import java.net.SocketTimeoutException

  class ResponseHandler private constructor(){
      private val firebaseCrashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

      companion object {
         @JvmStatic
         fun getInstance() = ResponseHandler()
      }


    private val TAG = ResponseHandler::class.java.simpleName

    fun <T : Any> handleSuccess(data: T): Resource<T> {
        return Resource.success(data)
    }

    fun <T > handleRequestException(e: Exception, string: String): Resource<T> {
        Log.e(TAG,"Error with request $string")
        logException(e)
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
    private fun logException(e:Exception){
        e.printStackTrace()
        if(!BuildConfig.DEBUG){
           firebaseCrashlytics.recordException(e)
        }
    }
}