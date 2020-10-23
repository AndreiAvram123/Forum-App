package com.andrew

import com.andrew.bookapp.AuthInterceptor
import com.andrew.dataLayer.models.serialization.RegisterUserDTO
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestUtilities {
    companion object {

        const val testUserID = 109
        const val testPostID = 2
        const val firstPostIDdb = 2
        const val lastPostIDDB = 1023
        const val testChatID = 1


        private val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE2MDE2MzU4MzcsImV4cCI6MTYzMzE3MTgzNywicm9sZXMiOlsiUk9MRV9VU0VSIl0sInVzZXJuYW1lIjoiYXZyYW1hbmRyZWl0aWJlcml1dXVAZ21haWwuY29tIn0.Mz_oamt_eIsJusYcLYu1TQFgsh8DoasymRFxA2YKA76Grdg-KvbGFKBlHzZpAw3TCfOEdb9kMARDwSOyWSPfaFWmPu4CJlceM3_dueHSNARTkr19Q1CNfq1zwnvxeK7JK1bOKKu4akJvq8UbqccZcRSGc95s64fP9I9AsIdhmSudo7udFwMHigtLLv0u94piOmAEU_Q64s6C2Wj7MZwFzn0Dak85hhMFpUqVHwAIJD1acYmUg8pen7SGQdSFQCqbsT1SGKWNiSGhK1fqJm56od39TD6G-eFYrZrShNDvtADxzbJml2ca_w7Ux-hdQQPcfpcMyIGxcaNWWaRKEkcSjRFAj6GzIAKPvniwlP2_BYfnP6RCY-iCNYqvQEiV5JUvSCUvVYQOJzlrCDd2T3evh9SgvEfzkEAXZDkxdKo_qre6OClcJFalAnvx0OcVPuNZhp37FuH905A-SlYsFrRYIfLxps97zb7OXwjk_un8xtLfJx4D0VkZo5Y8pQF7HiRCv1uKdGnXI6qcYG0sa2wUaoEa5NwyB0B8Ls4inhOnqQm2UDjUTnI09M8Yrp0c95k-kJjuG5aSuIDDOkxCnoVvCkNt2c-J8yHUo-YG3kj6CIaCb1XMhT3d4JlnrDVsBhCrOIOiWGE--VghqEgYPnUAR63AdSWEPaJ8EkP1RKnQK5M"
        private val retrofitClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(token)).build()



        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.andreiram.co.uk")
                .client(retrofitClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()



    }
}