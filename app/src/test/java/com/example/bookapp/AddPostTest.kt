package com.example.bookapp

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.example.TestUtilities
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.models.SerializeImage
import com.example.dataLayer.models.serialization.SerializePost
import junit.framework.Assert.fail
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

//stupid comment
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
                        userID = 3

                )
                val serverResponse = repo.uploadPost(uploadPost)
                val postID = serverResponse.message.toIntOrNull()

                if (postID == null) {
                    fail()
                } else {
                    val fetchedPost = repo.fetchPostByID(postID)
                    Assert.assertNotNull(fetchedPost)
                }

            }
        }
    }


}