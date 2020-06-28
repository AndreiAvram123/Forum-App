package com.example.bookapp.viewModels

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.bookapp.models.Post
import com.example.dataLayer.dataMappers.CommentMapper
import com.example.dataLayer.interfaces.CommentRepoInterface
import com.example.dataLayer.interfaces.dao.RoomCommentDao
import com.example.dataLayer.models.PostWithComments
import com.example.dataLayer.models.serialization.SerializeComment
import com.example.dataLayer.repositories.OperationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
class CommentsRepository @Inject constructor(private val connectivityManager: ConnectivityManager,
                                             private val commentDao: RoomCommentDao,
                                             private val repo: CommentRepoInterface) {


    fun getCommentsForPost(post: Post): LiveData<PostWithComments> = liveData {
     //   emitSource(commentDao.getAllPostComments(post.id))
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
           // val fetchedComments = repo.fetchCommentsForPost(post.id)
           // commentDao.insertComments(fetchedComments.map { CommentMapper.mapToDomainObject(it) })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}