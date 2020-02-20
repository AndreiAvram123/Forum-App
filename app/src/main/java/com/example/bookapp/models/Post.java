package com.example.bookapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {

    private int postID;
    private String postTitle;
    private String postDate;
    private String postAuthor;
    private String postImage;
    private String postCategory;
    private boolean isSaved = false;


    Post(int postID, String postTitle, String postDate, String postAuthor, String postCategory, String postImage) {
        this.postID = postID;
        this.postTitle = postTitle;
        this.postDate = postDate;
        this.postAuthor = postAuthor;
        this.postImage = postImage;
        this.postCategory = postCategory;

    }
    Post(int postID,String postTitle,String postImage){
        this.postID =postID;
        this.postTitle = postTitle;
        this.postImage = postImage;
    }

    private Post(Parcel in) {
        postID = in.readInt();
        postTitle = in.readString();
        postDate = in.readString();
        postAuthor = in.readString();
        postImage = in.readString();
        postCategory = in.readString();
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
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }
}


