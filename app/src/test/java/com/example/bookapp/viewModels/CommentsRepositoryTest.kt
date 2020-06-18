package com.example.bookapp.viewModels

import com.example.TestUtilities
import com.example.dataLayer.interfaces.CommentRepoInterface
import com.example.dataLayer.models.serialization.SerializeComment
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test

class CommentsRepositoryTest {

    private val repo: CommentRepoInterface = TestUtilities.retrofit
            .create(CommentRepoInterface::class.java)

    @Test
    fun shouldUploadedPostReturnId() = runBlocking {
        val commentToUpload = SerializeComment(content = "test content",
                postID = 1108,
                userID = 4)
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