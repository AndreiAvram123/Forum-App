package com.example.bookapp.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.Comment
import com.example.bookapp.models.Post
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.dataMappers.CommentMapper
import com.example.dataLayer.dataObjectsToSerialize.CommentDTO
import com.example.dataLayer.interfaces.CommentsInterface
import com.example.dataLayer.interfaces.dao.RoomCommentDao
import com.example.dataLayer.models.PostWithComments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class CommentsRepository(private val application: Application, private val coroutineScope: CoroutineScope) {
    private val retrofit = AppUtilities.getRetrofit()
    private val commentsInterface: CommentsInterface = retrofit.create(CommentsInterface::class.java)
    private val dao: RoomCommentDao = PostDatabase.getDatabase(application).commentDao();

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


    suspend fun uploadComment(commentDTO: CommentDTO) {

        try {
            val comment: Comment = commentsInterface.uploadComment(true, commentDTO);
            addCommentAndNotify(comment);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addCommentAndNotify(comment: Comment?) {
//        if (postComments != null && postComments!!.value != null) {
//            val newData = ArrayList(postComments!!.value!!)
//            newData.add(comment)
//            postComments!!.value = newData
//        }
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