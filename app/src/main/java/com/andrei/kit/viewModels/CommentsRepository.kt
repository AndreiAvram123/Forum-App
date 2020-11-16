package com.andrei.kit.viewModels

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.andrei.kit.models.Post
import com.andrei.dataLayer.dataMappers.toComment
import com.andrei.dataLayer.engineUtils.Resource
import com.andrei.dataLayer.engineUtils.ResponseHandler
import com.andrei.dataLayer.interfaces.CommentRepoInterface
import com.andrei.dataLayer.interfaces.dao.RoomCommentDao
import com.andrei.dataLayer.models.PostWithComments
import com.andrei.dataLayer.models.serialization.SerializeComment
import com.andrei.kit.utils.isConnected
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject


class CommentsRepository @Inject constructor(private val connectivityManager: ConnectivityManager,
                                             private val commentDao: RoomCommentDao,
                                             private val repo: CommentRepoInterface,
                                             private val responseHandler: ResponseHandler) {



    fun getCommentsForPost(post: Post): LiveData<PostWithComments> = liveData {
        emitSource(commentDao.getAllPostComments(post.id))
        if (connectivityManager.isConnected()) {
            fetchCommentsForPost(post)
        }
    }


    fun uploadComment(comment: SerializeComment) = liveData {
        emit(Resource.loading<Any>())
        try {
            val fetchedData  = repo.uploadComment(comment)
                commentDao.insertComment(fetchedData.toComment())
                emit(responseHandler.handleSuccess(Any()))

        } catch (e: Exception) {
            emit(responseHandler.handleException<Any>(e, "Upload comment"))
        }
    }


    private suspend fun fetchCommentsForPost(post: Post) {
        try {
            val fetchedComments = repo.fetchCommentsForPost(post.id)
            commentDao.insertComments(fetchedComments.map { it.toComment() })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}