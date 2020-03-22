package com.example.bookapp.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.AppUtilities;
import com.example.bookapp.models.Comment;
import com.example.dataLayer.dataObjectsToSerialize.SerializeComment;

import java.util.ArrayList;

public class ViewModelComments extends ViewModel {
    private CommentsRepository commentsRepository;
    private MutableLiveData<ArrayList<Comment>> currentPostComments;
    public ViewModelComments() {
        super();
        commentsRepository = CommentsRepository.getInstance(AppUtilities.getRetrofit());
    }

    public void uploadComment(@NonNull SerializeComment serializeComment) {
        commentsRepository.uploadComment(serializeComment);
    }

    public MutableLiveData<ArrayList<Comment>> getCommentsForPost(long postID) {
        currentPostComments = commentsRepository.fetchCommentsForPost(postID);

        return currentPostComments;
    }
}
