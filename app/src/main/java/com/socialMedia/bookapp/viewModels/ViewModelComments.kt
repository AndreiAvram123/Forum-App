package com.socialMedia.bookapp.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.socialMedia.bookapp.models.Post
import com.socialMedia.dataLayer.models.PostWithComments
import com.socialMedia.dataLayer.models.serialization.CommentDTO
import com.socialMedia.dataLayer.repositories.OperationStatus
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class ViewModelComments @ViewModelInject constructor(
        private val commentsRepository: CommentsRepository
) : ViewModel() {



    fun uploadComment(comment: CommentDTO): LiveData<OperationStatus> = commentsRepository.uploadComment(comment)

    fun getCommentsForPost(post: Post): LiveData<PostWithComments> = commentsRepository.getCommentsForPost(post);


}