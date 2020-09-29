package com.andrew.bookapp.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.andrew.bookapp.models.Post
import com.andrew.dataLayer.models.PostWithComments
import com.andrew.dataLayer.models.serialization.SerializeComment
import com.andrew.dataLayer.repositories.OperationStatus
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class ViewModelComments @ViewModelInject constructor(
        private val commentsRepository: CommentsRepository
) : ViewModel() {



    fun uploadComment(comment: SerializeComment): LiveData<OperationStatus> = commentsRepository.uploadComment(comment)

    fun getCommentsForPost(post: Post): LiveData<PostWithComments> = commentsRepository.getCommentsForPost(post);


}