package com.andrei.dataLayer.engineUtils

import java.lang.Exception

sealed class Result<out T > {

    data class Success<out T >(val data: T) : Result<T>()
    object Loading : Result<Nothing>()
    data class Error(val exception: Exception) : Result<Nothing>()

}