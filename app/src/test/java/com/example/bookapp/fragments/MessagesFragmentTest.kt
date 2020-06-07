package com.example.bookapp.fragments

import android.os.Build
import com.example.bookapp.AppUtilities
import com.example.bookapp.R
import com.example.bookapp.activities.WelcomeActivity
import com.example.dataLayer.interfaces.ChatRepositoryInterface
import com.example.dataLayer.models.serialization.SerializeMessage
import com.example.dataLayer.serverConstants.MessageTypes
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@InternalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class MessagesFragmentTest {

    private val repo = AppUtilities.getRetrofit().create(ChatRepositoryInterface::class.java)
    private lateinit var activity: WelcomeActivity

    @Before
    @Throws(Exception::class)
    fun setUp() {
        activity = Robolectric.buildActivity(WelcomeActivity::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun uploadedImageShouldReturnImagePath() {
        runBlocking {
            val drawable = activity.applicationContext.getDrawable(R.drawable.placeholder)
            drawable?.let {
                val base64Image = AppUtilities.getBase64ImageFromDrawable(it)
                val localID: Int = Calendar.getInstance().timeInMillis.hashCode()
                val message = SerializeMessage(type = MessageTypes.imageMessageType,
                        chatID = 12,
                        senderID = 109,
                        content = base64Image,
                        localIdentifier = localID.toString())
                val serverResponse = repo.pushMessage(message)
                Assert.assertTrue(serverResponse.successful)
            }

        }
    }
}