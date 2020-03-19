package com.example.bookapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;

        Post post = (Post) o;

        if (postID != post.postID) return false;
        if (!Objects.equals(postTitle, post.postTitle))
            return false;
        if (!Objects.equals(postDate, post.postDate))
            return false;
        return Objects.equals(postImage, post.postImage);
    }

    @Override
    public int hashCode() {
        int result = (int) (postID ^ (postID >>> 32));
        result = 31 * result + (postTitle != null ? postTitle.hashCode() : 0);
        result = 31 * result + (postDate != null ? postDate.hashCode() : 0);
        result = 31 * result + (postImage != null ? postImage.hashCode() : 0);
        return result;
    }
}


