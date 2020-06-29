package com.socialMedia.bookapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.socialMedia.dataLayer.models.PostDTO
import com.socialMedia.dataLayer.models.UserDTO
import com.socialMedia.dataLayer.models.serialization.CommentDTO
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class CommentsRepositoryTest {

    private val collection = "comments"
    private val repo = FirebaseFirestore.getInstance()

    @Test
    fun shouldUploadComment() = runBlocking<Unit> {
        val user = UserDTO("andrei","cactus@gmail.com","test...")
        val uploadComment = CommentDTO(postID = "6iuNOR94TdoiEy0oB3Az", date = Calendar.getInstance().timeInMillis / 1000,
                content = "text", user = user)
        val id = repo.collection(collection).add(uploadComment).await().id
        val fetchedDocument = repo.collection(collection).document(id).get().await()
        assertNotNull(fetchedDocument)
    }

    @Test
    fun shouldReturnPostComments() = runBlocking<Unit>{
        val postID = "6iuNOR94TdoiEy0oB3Az"
        val fetchedData = repo.collection(collection).whereEqualTo(CommentDTO::postID.name,postID).get().await()
        val mappedData = fetchedData.mapNotNull { it.toObject(CommentDTO::class.java) }
        assertNotNull(mappedData.isNotEmpty())
    }


    
}