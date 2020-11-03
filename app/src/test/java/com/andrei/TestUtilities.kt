package com.andrei

import android.content.Context
import android.net.ConnectivityManager
import androidx.test.core.app.ApplicationProvider
import com.andrei.dataLayer.engineUtils.AuthInterceptor
import com.andrei.kit.models.Post
import com.andrei.kit.models.User
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestUtilities {
    companion object {

        const val testUserID = "XdPbo623a2StgCOmyTREHzQOgFt1"
        const val testUserID2 = "aaaaaaa"

        const val testPostID = 3

        const val lastPostIDDB = 1023
        const val testChatID = 8

        val testPost = Post(id = testPostID,title = "testTitle",image = "testImage",date = System.currentTimeMillis(),
        content = "testContent",isFavorite = false,authorID = testUserID)

        val testUser = User(userID = testUserID,username = "testUsername",email = "testEmail",profilePicture = "testProfilePicture")
        private val context = ApplicationProvider.getApplicationContext<Context>()


       private val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE2MDM0NTM3MTIsImV4cCI6MTYzNDk4OTcxMiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sInVzZXJuYW1lIjoiYXZyYW1hbmRyZWlAeWFob28uY29tIn0.jzwxtOmXcgEbcQcWvIvR81nZc9KKtsrm6nDrQZFOFs0iCctzh4PqMfdLYdd0PcRCR32d_X8_bNAmZamAAiA_Eie4YMtu6Kga0itCzLTBUTWhvzL5r59QL-4BgFm6RUEwvtO5lK1xrlgiVXQHcldHmMXUvgy4IgQdNP1078OuPevNw4rCQDJPPSaMvkdVPJdpznBKNXc0vlrfYpcZNslAxIgBPm_RKXt6zDeurIlv5GAdp_O5DepR8qKHzsdwoYb6swmW4SHw__j9P9Sz8AdbKUfq6IZ04BNN9cMuJ6krWEWHbyMrT2LLLxzuVB4XZxJY4y6WFwWSgA5lMbJ4uv6g5QL72c5ygT5dZuhna-GXkH1XNYavpjfWXs4yYdFOJMzR3vtK1Azp42ZanB6zDrwBDSUMn5hCPuHP7hnGTRJdINY-q3fFvNpZXm6TMHnJxwCDXKPjDbjOGHGyLv8LlD_lpNaoiT8rx4XK2b2Hu6dEJauJBWtyCQyiCsFng_NqBVHX02IN6vXba7pHnbeRscd6ofQu4_32FarwdOn-5rcQ0n8RCa7UUfrIr8jCyIgb_c3S294RpUpaJcmH4_k1a4Iy3w3XzQLr2Yrz-THnPWNRF0nIII44zhzkH2ECcAg1Ijo8WFps2iAD0gUfnrqHRzvB4-lwJJhRwLlLrqLGTZb-qQk"

        private val retrofitClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(token, context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)).build()



        val retrofit:Retrofit = Retrofit.Builder()
                .baseUrl("http://www.andreiram.co.uk")
                .client(retrofitClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()



    }
}