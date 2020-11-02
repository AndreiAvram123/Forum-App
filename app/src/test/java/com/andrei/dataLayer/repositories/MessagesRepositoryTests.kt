package com.andrei.dataLayer.repositories

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.andrei.TestUtilities
import com.andrei.bookapp.R
import com.andrei.bookapp.toBase64
import com.andrei.dataLayer.interfaces.ChatRepositoryInterface
import com.andrei.dataLayer.models.serialization.SerializeMessage
import com.andrei.dataLayer.serverConstants.MessageTypes
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
                        chatID = TestUtilities.testChatID,
                        senderID = TestUtilities.testUserID,
                        content = it.toBase64(),
                        localIdentifier = localID.toString())
                val serverResponse = repo.pushMessage(message)
                Assert.assertTrue(serverResponse.successful)
            }

        }
    }
}