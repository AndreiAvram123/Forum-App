package com.example.bookapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.bookapp.models.Post
import com.example.dataLayer.dataObjectsToSerialize.CommentDTO
import com.example.dataLayer.models.PostWithComments
import com.example.dataLayer.models.serialization.SerializeComment
import com.example.dataLayer.repositories.UploadProgress
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class ViewModelComments(application: Application) : AndroidViewModel(application) {

    private val commentsRepository: CommentsRepository by lazy {
        CommentsRepository(application = getApplication(), coroutineScope = viewModelScope)
    }

    fun uploadComment(comment: SerializeComment): LiveData<UploadProgress> = commentsRepository.uploadComment(comment)

    fun getCommentsForPost(post: Post): LiveData<PostWithComments> =  commentsRepository.getCommentsForPost(post);


}