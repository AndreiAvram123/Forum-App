package com.example

import com.example.bookapp.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestUtilities {
    companion object {

        private val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE1OTMyMDk3MzksImV4cCI6MTYyNDc0NTczOSwicm9sZXMiOlsiUk9MRV9VU0VSIl0sInVzZXJuYW1lIjoiYWRtaW5AYWRtaW4uY29tIn0.dS-BREkUNQvnEOskf3ooSiC2mJFEMe_kGO8jY84CbHEmmmVexmZFEXOI2kz_ejDyoGrk1dXomdh9e84SU2s0DJDHJRFO5TQ8X0J8enpM9eoPJMuS9qmrHseVtOpb1jE-auE56U13DtnAnqw3XoMp_q_V-i-C82RITvJux9rPxXWofOB-z9xvA7t8e-EV4DiV5ArPxYb54NlwssUXh49Xy-5kMKvL42YTn3jyn8dNiocqcwOe8m12eZjcWf0Q5ylQbKTGuYvGYPh_5jNUxYiif-6I9ynFx5tZ0wEK2UD-mmsOx76obIU-muOtTxFZy8_tdMjCK5xCd9sfOEbnRC-oBMGPQvJdm8frI7RanbJ8kRe1aWEtXG4KAOahLzk2lQwMOY4a7Juz4nZsBNVF8LCNJle0yx4v_P5z_kXHdMszOm_eLaJfCrv4mv3sFh5RWL96AH67UrJpRzEkXSxOdl332sFRNVajqGG0zMatbt0dbDR2o6O42yTFxlgNG3NsyanXY_TOF-_m6Bj5GoP3X58NiD0IXvBuBnwBF2EIs2wyFCvfh__Fc-D0yyxSqxUfVJaPg49QNT1faYlzW2537thA7QX84qOH2e-lwMN5QinUqRa5VGFlIIsoiBbXH4XMUJ6R07-T_Sy8ztPxNjEtD1pyrV9JHqRMSSsZT6KOjzkULlQ"
        private val retrofitClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(token)).build()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.andreiram.co.uk")
                .client(retrofitClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}