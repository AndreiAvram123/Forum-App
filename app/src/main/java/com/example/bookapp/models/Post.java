package com.example.bookapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class Post implements Parcelable {

    private int postID;
    private String postTitle;
    private String postDate;
    private String postAuthor;
    private String postImage;
    private String postCategory;
    private String postContent;
    private boolean isSaved = false;

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

    //    Post(int postID, String postTitle, String postDate, String postAuthor, String postCategory, String postImage, String postContent) {
//        this(postID, postTitle, postImage, postAuthor);
//        this.postDate = postDate;
//        this.postCategory = postCategory;
//        this.postContent = postContent;
//
//    }
//
//
//    Post(int postID, String postTitle, String postImage, String postAuthor) {
//        this.postID = postID;
//        this.postTitle = postTitle;
//        this.postImage = postImage;
//        this.postAuthor = postAuthor;
//    }
//
    private Post(Parcel in) {
        postID = in.readInt();
        postTitle = in.readString();
        postDate = in.readString();
        postAuthor = in.readString();
        postImage = in.readString();
        postCategory = in.readString();
        postContent = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(postID);
        dest.writeString(postTitle);
        dest.writeString(postDate);
        dest.writeString(postAuthor);
        dest.writeString(postImage);
        dest.writeString(postCategory);
        dest.writeString(postContent);
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
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


