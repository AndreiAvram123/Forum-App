package com.example.bookapp.models;

public class PostBuilder {
    private int postID;
    private String postTitle;
    private String postDate;
    private String postAuthor;
    private String postImage;

    public PostBuilder setPostID(int postID) {
        this.postID = postID;
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
        return new Post(postID, postTitle, postDate, postAuthor, postImage);
    }
}