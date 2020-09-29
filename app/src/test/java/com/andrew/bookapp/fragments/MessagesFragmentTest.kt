package com.andrew.bookapp.fragments

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.andrew.TestUtilities
import com.andrew.bookapp.R
import com.andrew.bookapp.toBase64
import com.andrew.dataLayer.interfaces.ChatRepositoryInterface
import com.andrew.dataLayer.models.serialization.SerializeMessage
import com.andrew.dataLayer.serverConstants.MessageTypes
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@InternalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class MessagesFragmentTest {

    private val repo = TestUtilities.retrofit.create(ChatRepositoryInterface::class.java)
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun uploadedImageShouldReturnImagePath() {
        runBlocking {
            val drawable = context.getDrawable(R.drawable.placeholder)
            drawable?.let {
                val localID: Int = Calendar.getInstance().timeInMillis.hashCode()
                val message = SerializeMessage(type = MessageTypes.imageMessageType,
                        chatID = 16,
                        senderID = 109,
                        content = it.toBase64(),
                        localIdentifier = localID.toString())
                val serverResponse = repo.pushMessage(message)
                Assert.assertTrue(serverResponse.successful)
            }

        }
    }
}