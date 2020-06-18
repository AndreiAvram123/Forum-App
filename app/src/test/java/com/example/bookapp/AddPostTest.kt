package com.example.bookapp

import android.os.Build
import com.example.TestUtilities
import com.example.bookapp.activities.WelcomeActivity
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.models.SerializeImage
import com.example.dataLayer.models.serialization.SerializePost
import junit.framework.Assert.fail
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@InternalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class AddPostTest {


    private val repo = TestUtilities.retrofit.create(PostRepositoryInterface::class.java)
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
    fun shouldUploadImageToServer() {
        runBlocking {
            val drawable = activity.applicationContext.getDrawable(R.drawable.placeholder)
            drawable?.let {
                val image = SerializeImage(imageData = it.toBase64(), extension = null)
                repo.uploadImage(image)
            }

        }
    }


    @Test
    fun uploadedPostShouldBeFoundOnServer() {
        runBlocking {
            val drawable = activity.applicationContext.getDrawable(R.drawable.placeholder)
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