package com.andrei.dataLayer.repositories

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.andrei.TestUtilities
import com.andrei.kit.R
import com.andrei.kit.toBase64
import com.andrei.dataLayer.interfaces.ChatRepositoryInterface
import com.andrei.dataLayer.models.serialization.SerializeMessage
import com.andrei.dataLayer.serverConstants.MessageTypes
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.fail
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
    fun uploadedImageShouldReturnResult() {
        runBlocking {
            val drawable = context.getDrawable(R.drawable.placeholder)
            drawable?.let {
                val message = SerializeMessage(type = MessageTypes.imageMessageType,
                        chatID = TestUtilities.testChatID,
                        senderID = TestUtilities.testUserID,
                        content = it.toBase64())
                try {
                    val messageResponse = repo.pushMessage(message)
                }catch (e:Exception){
                    e.printStackTrace()
                    fail()
                }
            }

        }
    }
}