package com.andrew.bookapp

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.andrew.TestUtilities
import com.andrew.dataLayer.interfaces.PostRepositoryInterface
import com.andrew.dataLayer.models.SerializeImage
import com.andrew.dataLayer.models.serialization.SerializePost
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@InternalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class AddPostTest {

    private val repo = TestUtilities.retrofit.create(PostRepositoryInterface::class.java)
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun shouldUploadImageToServer() {
        runBlocking {
            val drawable = context.getDrawable(R.drawable.placeholder)
            drawable?.let {
                val image = SerializeImage(imageData = it.toBase64(), extension = null)
                repo.uploadImage(image)
            }

        }
    }


    @Test
    fun uploadedPostShouldBeFoundOnServer() {
        runBlocking {
            val drawable = context.applicationContext.getDrawable(R.drawable.placeholder)
            drawable?.let {

                val image = SerializeImage(imageData = it.toBase64(), extension = null)

                val response = repo.uploadImage(image)

                val imageLink = response.message

                val uploadPost = SerializePost(
                        title = "Placeholder title",
                        content = "Placeholder content",
                        image = imageLink,
                        userID = TestUtilities.testUserID

                )
                val serverResponse = repo.uploadPost(uploadPost)
                val postID = serverResponse.message.toIntOrNull()

                if (postID == null) {
                    Assert.fail()
                } else {
                    val fetchedPost = repo.fetchPostByID(postID)
                    Assert.assertNotNull(fetchedPost)
                }

            }
        }
    }


}