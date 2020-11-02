package com.andrei.kit.viewModels

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.andrei.kit.models.Post
import com.andrei.dataLayer.dataMappers.CommentMapper
import com.andrei.dataLayer.interfaces.CommentRepoInterface
import com.andrei.dataLayer.interfaces.dao.RoomCommentDao
import com.andrei.dataLayer.models.PostWithComments
import com.andrei.dataLayer.models.serialization.SerializeComment
import com.andrei.dataLayer.repositories.OperationStatus
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class CommentsRepository @Inject constructor(private val connectivityManager: ConnectivityManager,
                                             private val commentDao: RoomCommentDao,
                                             private val repo: CommentRepoInterface) {


    fun getCommentsForPost(post: Post): LiveData<PostWithComments> = liveData {
        emitSource(commentDao.getAllPostComments(post.id))
        if (connectivityManager.activeNetwork != null) {
            fetchCommentsForPost(post)
        }
    }


    fun uploadComment(comment: SerializeComment) = liveData {
        emit(OperationStatus.ONGOING)
        try {
            val serverResponse = repo.uploadComment(comment)
            val commentID = serverResponse.message.toIntOrNull()
            commentID?.let {
                val fetchedComment = repo.fetchCommentById(it)
                commentDao.insertComment(CommentMapper.mapToDomainObject(fetchedComment))
                emit(OperationStatus.FINISHED)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            emit(OperationStatus.FAILED)
        }
    }


    private suspend fun fetchCommentsForPost(post: Post) {
        try {
            val fetchedComments = repo.fetchCommentsForPost(post.id)
            commentDao.insertComments(fetchedComments.map { CommentMapper.mapToDomainObject(it) })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}