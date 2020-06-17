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
import javax.inject.Inject

@InternalCoroutinesApi
class ViewModelComments(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var commentsRepository: CommentsRepository


    fun uploadComment(comment: SerializeComment): LiveData<UploadProgress> = commentsRepository.uploadComment(comment)

    fun getCommentsForPost(post: Post): LiveData<PostWithComments> = commentsRepository.getCommentsForPost(post);


}