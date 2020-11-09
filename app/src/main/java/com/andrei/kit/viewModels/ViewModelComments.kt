package com.andrei.kit.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.andrei.dataLayer.engineUtils.Resource
import com.andrei.kit.models.Post
import com.andrei.dataLayer.models.PostWithComments
import com.andrei.dataLayer.models.serialization.SerializeComment
import kotlinx.coroutines.InternalCoroutinesApi

class ViewModelComments @ViewModelInject constructor(
        private val commentsRepository: CommentsRepository
) : ViewModel() {



    fun uploadComment(comment: SerializeComment): LiveData<Resource<Any>> = commentsRepository.uploadComment(comment)

    fun getCommentsForPost(post: Post): LiveData<PostWithComments> = commentsRepository.getCommentsForPost(post);


}