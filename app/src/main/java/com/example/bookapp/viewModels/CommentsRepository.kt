package com.example.bookapp.viewModels

import androidx.lifecycle.MutableLiveData
import com.example.bookapp.models.Comment
import com.example.dataLayer.dataObjectsToSerialize.SerializeComment
import com.example.dataLayer.interfaces.CommentsInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception
import java.util.*

internal class CommentsRepository private constructor(retrofit: Retrofit) {
    private val commentsInterface: CommentsInterface
    private var postComments: MutableLiveData<ArrayList<Comment?>?>? = null


   suspend fun uploadComment(comment: SerializeComment) {

       try{
           val comment:Comment = commentsInterface.uploadComment(true,comment);
           addCommentAndNotify(comment);
       }catch (e:Exception){
           e.printStackTrace()
       }
    }

    private fun addCommentAndNotify(comment: Comment?) {
        if (postComments != null && postComments!!.value != null) {
            val newData = ArrayList(postComments!!.value!!)
            newData.add(comment)
            postComments!!.value = newData
        }
    }

   suspend fun fetchCommentsForPost(postID: Long): MutableLiveData<ArrayList<Comment?>?> {
        postComments = MutableLiveData()
        try{
            postComments?.value = commentsInterface.fetchCommentsByPostID(postID) ;
        }catch (e:Exception){

        }
        return postComments!!
    }

    companion object {
        private var instance: CommentsRepository? = null

        @JvmStatic
        @Synchronized
        fun getInstance(retrofit: Retrofit): CommentsRepository? {
            if (instance == null) {
                instance = CommentsRepository(retrofit)
            }
            return instance
        }
    }

    init {
        commentsInterface = retrofit.create(CommentsInterface::class.java)
    }
}