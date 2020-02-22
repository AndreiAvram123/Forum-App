package com.example.bookapp.models;

public class NonUploadedPost {

    private String postTitle;
    private String postContent;
    private String postCategory;
    private String imageName;
    private byte[] imageBytes;

    NonUploadedPost(String postTitle, String postContent, String postCategory, String imageName, byte[] imageBytes) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postCategory = postCategory;
        this.imageName = imageName;
        this.imageBytes = imageBytes;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public String getImageName() {
        return imageName;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

}
