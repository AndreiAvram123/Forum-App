package com.example.bookapp.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.Comment
import com.example.bookapp.models.Post
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.dataMappers.CommentMapper
import com.example.dataLayer.dataObjectsToSerialize.CommentDTO
import com.example.dataLayer.interfaces.CommentsInterface
import com.example.dataLayer.interfaces.dao.RoomCommentDao
import com.example.dataLayer.models.PostWithComments
import com.example.dataLayer.models.serialization.SerializeComment
import com.example.dataLayer.repositories.UploadProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class CommentsRepository(private val application: Application, private val coroutineScope: CoroutineScope) {
    private val commentsInterface: CommentsInterface = AppUtilities.getRetrofit().create(CommentsInterface::class.java)
    private val dao: RoomCommentDao = PostDatabase.getDatabase(application).commentDao();
    private val uploadProgress = MutableLiveData<UploadProgress>()

    //maybe have a dictionary
    private val currentlyFetchedComments: HashMap<Post, LiveData<PostWithComments>> = HashMap()

    fun getCommentsForPost(post: Post): LiveData<PostWithComments> {
        currentlyFetchedComments[post] = dao.getAllPostComments(post.id)
        if (AppUtilities.isNetworkAvailable(application)) {
            coroutineScope.launch {
                fetchCommentsForPost(post)
            }
        }
        return currentlyFetchedComments[post]!!;
    }


    fun uploadComment(comment: SerializeComment): LiveData<UploadProgress> {
        uploadProgress.value = UploadProgress.UPLOADING
        coroutineScope.launch {
            try {

                val serverResponse = commentsInterface.uploadComment(comment)
                val commentID = serverResponse.message.toIntOrNull()
                commentID?.let {
                    val fetchedComment = commentsInterface.fetchCommentById(it)
                    dao.insertComment(CommentMapper.mapDtoObjectToDomainObject(fetchedComment))
                }
                uploadProgress.postValue(UploadProgress.UPLOADED)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return uploadProgress
    }


    private suspend fun fetchCommentsForPost(post: Post) {
        try {
            val fetchedComments = commentsInterface.fetchCommentsForPost(post.id)
            dao.insertComments(CommentMapper.mapDTOObjectsToDomainObjects(fetchedComments))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}