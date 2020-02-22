//package com.example.bookapp.models;
//
//public class PostBuilder {
//    private int postID;
//    private String postTitle;
//    private String postDate;
//    private String postAuthor;
//    private String postImage;
//    private String postCategory;
//    private String postContent;
//
//    public PostBuilder setPostContent(String postContent) {
//        this.postContent = postContent;
//        return this;
//    }
//
//    public PostBuilder setPostID(int postID) {
//        this.postID = postID;
//        return this;
//    }
//
//    PostBuilder setPostCategory(String postCategory) {
//        this.postCategory = postCategory;
//        return this;
//    }
//
//
//    PostBuilder setPostTitle(String postTitle) {
//        this.postTitle = postTitle;
//        return this;
//    }
//
//    PostBuilder setPostDate(String postDate) {
//        this.postDate = postDate;
//        return this;
//    }
//
//    PostBuilder setPostAuthor(String postAuthor) {
//        this.postAuthor = postAuthor;
//        return this;
//    }
//
//    PostBuilder setPostImage(String postImage) {
//        this.postImage = postImage;
//        return this;
//    }
//
//
//    public Post createPost() {
//        if (postID == 0) {
//            throw new IllegalStateException("Post id must not be 0");
//        }
//        if (postTitle == null) {
//            throw new IllegalStateException("Post title must not be null");
//        }
//        if (postImage == null) {
//            throw new IllegalStateException("Post image cannot be null");
//        }
//        if (postAuthor == null) {
//            throw new IllegalStateException("Post author cannot be null");
//        }
//
//        if (postContent == null) {
//            return new Post(postID, postTitle, postImage, postAuthor);
//        } else {
//            return new Post(postID, postTitle, postDate, postAuthor, postCategory, postImage,postContent);
//        }
//    }
//}