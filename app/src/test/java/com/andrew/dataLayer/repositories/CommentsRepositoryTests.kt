package com.andrew.dataLayer.repositories

import com.andrew.TestUtilities
import com.andrew.TestUtilities.Companion.testPostID
import com.andrew.TestUtilities.Companion.testUserID
import com.andrew.dataLayer.interfaces.CommentRepoInterface
import com.andrew.dataLayer.models.serialization.SerializeComment
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test

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

    @Test
    fun shouldReturnCommentFromId() = runBlocking{

        val fetchedPost = repo.fetchCommentById(11)
        assertNotNull(fetchedPost)
    }
}