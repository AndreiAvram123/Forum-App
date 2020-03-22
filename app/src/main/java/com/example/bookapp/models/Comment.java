package com.example.bookapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment implements Parcelable {

    @SerializedName("commentID")
    @Expose
    private int commentID;
    @SerializedName("commentDate")
    @Expose
    private String commentDate;
    @SerializedName("commentContent")
    @Expose
    private String commentContent;
    @SerializedName("commentAuthor")
    @Expose
    private String  commentAuthor;
    @SerializedName("commentUserID")
    private String commentUserID;


    private int postID;

    Comment(int commentID, String commentDate, String commentContent, String commentAuthor, int postID, String commentUserID) {
        this.commentID = commentID;
        this.commentDate = commentDate;
        this.commentContent = commentContent;
        this.commentAuthor = commentAuthor;
        this.postID =postID;
        this.commentUserID = commentUserID;
    }

    private Comment(Parcel in) {
        commentID = in.readInt();
        commentDate = in.readString();
        commentContent = in.readString();
        commentAuthor = in.readString();
        postID =in.readInt();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };


    public int getPostID() {
        return postID;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public int getCommentID() {
        return commentID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(commentID);
        dest.writeString(commentDate);
        dest.writeString(commentContent);
        dest.writeString(commentAuthor);
        dest.writeInt(postID);
    }
}
