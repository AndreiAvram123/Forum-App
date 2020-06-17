package com.example

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestUtilities {
    companion object {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.andreiram.co.uk/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}