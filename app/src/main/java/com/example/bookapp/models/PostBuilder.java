package com.example.bookapp.models;

import java.util.ArrayList;

public class PostBuilder {
    private int postID;
    private String postTitle;
    private String postDate;
    private String postAuthor;
    private String postImage;
    private String postCategory;

    public PostBuilder setPostID(int postID) {
        this.postID = postID;
        return this;
    }

    public PostBuilder setPostCategory(String postCategory) {
        this.postCategory = postCategory;
        return this;
    }


    public PostBuilder setPostTitle(String postTitle) {
        this.postTitle = postTitle;
        return this;
    }

    public PostBuilder setPostDate(String postDate) {
        this.postDate = postDate;
        return this;
    }

    public PostBuilder setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
        return this;
    }

    public PostBuilder setPostImage(String postImage) {
        this.postImage = postImage;
        return this;
    }


    public Post createPost() {
        if (postID == 0) {
            throw new IllegalStateException("Post id must not be 0");
        }
        if (postTitle == null) {
            throw new IllegalStateException("Post title must not be null");
        }
        if (postImage == null) {
            throw new IllegalStateException("Post image cannot be null");
        }
        if (postDate == null) {
            return new Post(postID, postTitle, postImage);
        } else {
            return new Post(postID, postTitle, postDate, postAuthor, postCategory, postImage);
        }
    }
}