package com.example.bookapp.viewModels

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.bookapp.models.Post
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.dataMappers.CommentMapper
import com.example.dataLayer.interfaces.CommentRepoInterface
import com.example.dataLayer.interfaces.dao.RoomCommentDao
import com.example.dataLayer.models.PostWithComments
import com.example.dataLayer.models.serialization.SerializeComment
import com.example.dataLayer.repositories.UploadProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
class CommentsRepository @Inject constructor(private val connectivityManager: ConnectivityManager,
                                             private val commentDao: RoomCommentDao,
                                             private val coroutineScope: CoroutineScope,
                                             private val repo: CommentRepoInterface) {


    //maybe have a dictionary
    private val currentlyFetchedComments: HashMap<Post, LiveData<PostWithComments>> = HashMap()

    fun getCommentsForPost(post: Post): LiveData<PostWithComments> {
        currentlyFetchedComments[post] = commentDao.getAllPostComments(post.id)
        if (connectivityManager.activeNetwork != null) {
            coroutineScope.launch {
                fetchCommentsForPost(post)
            }
        }
        return currentlyFetchedComments[post]!!
    }


    fun uploadComment(comment: SerializeComment) = liveData {
        emit(UploadProgress.UPLOADING)
        try {
            val serverResponse = repo.uploadComment(comment)
            val commentID = serverResponse.message.toIntOrNull()
            commentID?.let {
                val fetchedComment = repo.fetchCommentById(it)
                commentDao.insertComment(CommentMapper.mapToDomainObject(fetchedComment))
                emit(UploadProgress.UPLOADED)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            emit(UploadProgress.FAILED)
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