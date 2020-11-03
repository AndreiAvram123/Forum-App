package com.andrei.dataLayer.repositories

import android.os.Build
import com.andrei.TestUtilities
import com.andrei.TestUtilities.Companion.testPostID
import com.andrei.TestUtilities.Companion.testUserID
import com.andrei.dataLayer.interfaces.CommentRepoInterface
import com.andrei.dataLayer.models.serialization.SerializeComment
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class CommentsRepositoryTest {

    private val repo: CommentRepoInterface = TestUtilities.retrofit
            .create(CommentRepoInterface::class.java)

    @Test
    fun shouldUploadedPostReturnId() = runBlocking {
        val commentToUpload = SerializeComment(content = "test content",
                postID = testPostID,
                userID = testUserID)
        val response = repo.uploadComment(commentToUpload)
        val id = response.message.toIntOrNull()
        assertNotNull(id)
    }
    
}