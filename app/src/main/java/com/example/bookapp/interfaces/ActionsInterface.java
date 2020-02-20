package com.example.bookapp.interfaces;

import com.example.bookapp.models.Post;

public interface ActionsInterface {
    void sharePost(Post post);
    void savePost(Post post);
    void deleteSavedPost(Post post);
    void expandPost(int postID);
}
