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

    fun <T : Any> handleSuccess(data: T): Result<T> {
        return Result.Success(data)
    }

    fun <T > handleRequestException(e: Exception, string: String): Result<T> {
        Log.e(TAG,"Error with request $string")
        logException(e)
        return Result.Error(e)
    }

    private fun logException(e:Exception){
        e.printStackTrace()
        if(!BuildConfig.DEBUG){
           firebaseCrashlytics.recordException(e)
        }
    }
}