package com.example.bookapp.models;

public class NonUploadedPostBuilder {
    private String postTitle;
    private String postContent;
    private String postCategory;
    private String imageName;
    private byte[] imageBytes;

    public NonUploadedPostBuilder setPostTitle(String postTitle) {
        this.postTitle = postTitle;
        return this;
    }

    public NonUploadedPostBuilder setPostContent(String postContent) {
        this.postContent = postContent;
        return this;
    }

    public NonUploadedPostBuilder setPostCategory(String postCategory) {
        this.postCategory = postCategory;
        return this;
    }

    public NonUploadedPostBuilder setImageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public NonUploadedPostBuilder setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
        return this;
    }


    public NonUploadedPost createPostUnderUploadModel() {
        return new NonUploadedPost(postTitle, postContent, postCategory, imageName, imageBytes);
    }
}