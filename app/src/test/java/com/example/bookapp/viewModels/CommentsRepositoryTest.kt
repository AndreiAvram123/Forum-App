package com.example.bookapp.viewModels

import com.example.bookapp.AppUtilities
import com.example.dataLayer.interfaces.ChatInterface
import com.example.dataLayer.interfaces.CommentsInterface
import com.example.dataLayer.models.serialization.SerializeComment
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Test

class CommentsRepositoryTest {

    private val repo: CommentsInterface = AppUtilities.getRetrofit()
            .create(CommentsInterface::class.java)

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