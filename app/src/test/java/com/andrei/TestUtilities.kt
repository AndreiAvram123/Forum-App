package com.andrei

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.andrei.dataLayer.engineUtils.AuthInterceptor
import com.andrei.kit.R
import com.andrei.kit.models.Post
import com.andrei.kit.models.User
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestUtilities {
    companion object {

         val testUserID:String
         val testUserID2 :String
         val testPostID :Int
         val testChatID:Int
        const val lastPostIDDB = 1023
         val testPost:Post
        private val token:String
        val testUser:User

        private val context:Context = ApplicationProvider.getApplicationContext()
        private val retrofitClient: OkHttpClient
        val retrofit:Retrofit

        init{
            with(context){
                testUserID = getString(R.string.testUserID)
                testUserID2 = getString(R.string.testUserID2)
                testPostID = resources.getInteger(R.integer.testPostID)
                testChatID = resources.getInteger(R.integer.testChatID)
                token = getString(R.string.token)
            }

            with(context){
                testUser = User(
                        userID = testUserID,
                        username = getString(R.string.testUsername),
                        email = getString(R.string.testEmail),
                        profilePicture = getString(R.string.testProfilePicture))

            }

            with(context){
                testPost = Post(
                        id = testPostID,
                        user = testUser,
                        title = getString(R.string.testPostTitle),
                        images = getString(R.string.testPostImage),
                        content = getString(R.string.testPostContent),
                        date = System.currentTimeMillis(),
                        isFavorite = false,
                        numberOfComments = 0,
                        bookmarkTimes = 0
                )
            }

            retrofitClient = OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(token)).build()
            retrofit = Retrofit.Builder().baseUrl("http://www.andreiram.co.uk")
                    .client(retrofitClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
       }

    }
}