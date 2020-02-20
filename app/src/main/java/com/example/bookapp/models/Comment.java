package com.example.bookapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

    private int commentID;
    private String commentDate;
    private String commentContent;
    private String  commentAuthor;

    Comment(int commentID, String commentDate, String commentContent, String commentAuthor) {
        this.commentID = commentID;
        this.commentDate = commentDate;
        this.commentContent = commentContent;
        this.commentAuthor = commentAuthor;
    }

    private Comment(Parcel in) {
        commentID = in.readInt();
        commentDate = in.readString();
        commentContent = in.readString();
        commentAuthor = in.readString();
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

    public int getCommentID() {
        return commentID;
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
    }
}
