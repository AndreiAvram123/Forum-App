package com.example.bookapp

import android.content.Context
import android.net.ConnectivityManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppUtilities {

    val retrofitGsonConverter: Retrofit = Retrofit.Builder()
            .baseUrl("http://www.andreiram.co.uk/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    /**
     * This method uses a regex with the matches() method in
     * order to determine if the email address is valid or
     * not
     *
     * @param email
     * @return
     */
    fun isEmailValid(email: String): Boolean {
        val regex = "[a-zA-Z0-9]+@[a-z]+\\.[a-z]+"
        return email.matches(regex.toRegex())
    }

}