package com.example.bookapp.viewModels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.models.Post
import com.example.dataLayer.models.PostWithComments
import com.example.dataLayer.models.serialization.SerializeComment
import com.example.dataLayer.repositories.OperationStatus
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class ViewModelComments @ViewModelInject constructor(
        private val commentsRepository: CommentsRepository
) : ViewModel() {



    fun uploadComment(comment: SerializeComment): LiveData<OperationStatus> = commentsRepository.uploadComment(comment)

    fun getCommentsForPost(post: Post): LiveData<PostWithComments> = commentsRepository.getCommentsForPost(post);


}