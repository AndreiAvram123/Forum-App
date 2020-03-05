package com.example.bookapp.interfaces;

import com.example.bookapp.models.Comment;
import com.example.bookapp.models.NonUploadedPost;
import com.example.bookapp.models.Post;

public interface MainActivityInterface {
    void sharePost(Post post);

    void savePost(Post post);

    void deleteSavedPost(Post post);

    void expandPost(int postID);

    void uploadComment(Comment comment);

    void uploadPost(NonUploadedPost nonUploadedPost);

    void startChat(String userID);

    void fetchNewPosts();
}
