package com.example.bookapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("postID")
    @Expose
    private long postID;
    @SerializedName("postTitle")
    @Expose
    private String postTitle;
    @SerializedName("postDate")
    @Expose
    private String postDate;
    @SerializedName("postAuthor")
    @Expose
    private String postAuthor;
    @SerializedName("postImage")
    @Expose
    private String postImage;
    @SerializedName("postCategory")
    @Expose
    private String postCategory;
    @SerializedName("postContent")
    @Expose
    private String postContent;
    private boolean isFavorite = false;

    public String getPostCategory() {
        return postCategory;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostDate() {
        return postDate;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public String getPostImage() {
        return postImage;
    }

    public String getPostContent() {
        return postContent;
    }

    public long getPostID() {
        return postID;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }


}


