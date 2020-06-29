package com.socialMedia.bookapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.firebase.firestore.FirebaseFirestore
import com.socialMedia.bookapp.models.Post
import com.socialMedia.dataLayer.dataMappers.toComment
import com.socialMedia.dataLayer.interfaces.dao.RoomCommentDao
import com.socialMedia.dataLayer.models.PostWithComments
import com.socialMedia.dataLayer.models.serialization.CommentDTO
import com.socialMedia.dataLayer.repositories.OperationStatus
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@InternalCoroutinesApi
class CommentsRepository @Inject constructor(
        private val firebaseFirestore: FirebaseFirestore,
        private val commentDao: RoomCommentDao) {


    private val collectionName = "comments"

    fun getCommentsForPost(post: Post): LiveData<PostWithComments> = liveData {
        emitSource(commentDao.getAllPostComments(post.id))
        fetchCommentsForPost(post)
    }


    fun uploadComment(comment: CommentDTO) = liveData {
        emit(OperationStatus.ONGOING)
        try {
            val id = firebaseFirestore.collection(collectionName).add(comment).await().id
            val fetchedSnapshot = firebaseFirestore.collection(collectionName).document(id).get().await()
            val commentDTO = fetchedSnapshot.toObject(CommentDTO::class.java)
            commentDTO?.let {
                val comment = it.toComment()
                commentDao.insertComment(comment)
            }
            emit(OperationStatus.FINISHED)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(OperationStatus.FAILED)
        }
    }


    private suspend fun fetchCommentsForPost(post: Post) {
        try {
            // val fetchedComments = repo.fetchCommentsForPost(post.id)
            // commentDao.insertComments(fetchedComments.map { CommentMapper.mapToDomainObject(it) })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}