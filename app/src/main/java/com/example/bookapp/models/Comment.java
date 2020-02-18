package com.example.bookapp.models;

public class Comment {

    private int commentID;
    private String commentDate;
    private String commentContent;
    private String  commentAuthor;

    public Comment(int commentID, String commentDate, String commentContent, String commentAuthor) {
        this.commentID = commentID;
        this.commentDate = commentDate;
        this.commentContent = commentContent;
        this.commentAuthor = commentAuthor;
    }

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
}
