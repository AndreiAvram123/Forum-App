package com.example.bookapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.bookapp.models.Post
import com.example.dataLayer.dataObjectsToSerialize.CommentDTO
import com.example.dataLayer.models.PostWithComments
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class ViewModelComments(application: Application) : AndroidViewModel(application) {

    private val commentsRepository: CommentsRepository by lazy {
        CommentsRepository(application = getApplication(), coroutineScope = viewModelScope)
    }

    fun uploadComment(commentDTO: CommentDTO) {
        // commentsRepository.uploadComment(serializeComment);
    }


    fun getCommentsForPost(post: Post): LiveData<PostWithComments> {

        return commentsRepository.getCommentsForPost(post);
    }


}