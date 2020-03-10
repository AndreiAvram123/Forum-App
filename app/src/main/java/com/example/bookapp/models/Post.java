package com.example.bookapp.models;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class Post {

    private int postID;
    private String postTitle;
    private String postDate;
    private String postAuthor;
    private String postImage;
    private String postCategory;
    private String postContent;
    private boolean isFavorite = false;


    public static class PostBuilder {
        private int postID;
        private String postTitle;
        private String postDate;
        private String postAuthor;
        private String postImage;
        private String postCategory;
        private String postContent;


        public PostBuilder setPostContent(String postContent) {
            this.postContent = postContent;
            return this;
        }

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
            if (postAuthor == null) {
                throw new IllegalStateException("Post author cannot be null");
            }
            Post post = new Post();
            post.postID = postID;
            post.postTitle = postTitle;
            post.postImage = postImage;
            post.postAuthor = postAuthor;
            post.postDate = postDate;
            post.postCategory = postCategory;
            post.postImage = postImage;
            post.postContent = postContent;
            return post;
        }

    }


    public Post() {

    }

    public int getPostID() {
        return postID;
    }

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


    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (postID != post.postID) return false;
        if (!postTitle.equals(post.postTitle)) return false;
        if (!postAuthor.equals(post.postAuthor)) return false;
        return postImage.equals(post.postImage);
    }

    @Override
    public int hashCode() {
        int result = postID;
        result = 31 * result + postTitle.hashCode();
        result = 31 * result + postAuthor.hashCode();
        result = 31 * result + postImage.hashCode();
        return result;
    }
}


