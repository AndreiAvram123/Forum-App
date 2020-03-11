package com.example.bookapp.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.dataLayer.interfaces.CommentsInterface;
import com.example.bookapp.models.Comment;
import com.example.dataLayer.dataObjectsToSerialize.SerializeComment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

class CommentsRepository {
    private CommentsInterface commentsInterface;
    private static CommentsRepository instance;
    private MutableLiveData<ArrayList<Comment>> postComments;

    public static synchronized CommentsRepository getInstance(@NonNull Retrofit retrofit) {
        if (instance == null) {
            instance = new CommentsRepository(retrofit);
        }
        return instance;
    }

    private CommentsRepository(@NonNull Retrofit retrofit) {
        commentsInterface = retrofit.create(CommentsInterface.class);
    }

    void uploadComment(@NonNull SerializeComment comment) {
        commentsInterface.uploadComment(true, comment).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(@NonNull Call<Comment> call, @NonNull Response<Comment> response) {
                //the postComments and the value of post comments
                //should not be null but we should check
                addCommentAndNotify(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void addCommentAndNotify(Comment comment) {
        if (postComments != null && postComments.getValue() != null) {
            ArrayList<Comment> newData = new ArrayList<>(postComments.getValue());
            newData.add(comment);
            postComments.setValue(newData);
        }
    }

    @NonNull
    MutableLiveData<ArrayList<Comment>> fetchCommentsForPost(int postID) {
        postComments = new MutableLiveData<>();

        commentsInterface.fetchCommentsByPostID(postID, true).enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Comment>> call, @NonNull Response<ArrayList<Comment>> response) {
                postComments.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Comment>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        return postComments;
    }
}
