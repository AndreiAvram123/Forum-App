package com.example.bookapp.models;

public class CommentBuilder {
    private int commentID;
    private String commentDate;
    private String commentContent;
    private String commentAuthor;

    public CommentBuilder setCommentID(int commentID) {
        this.commentID = commentID;
        return this;
    }

    public CommentBuilder setCommentDate(String commentDate) {
        this.commentDate = commentDate;
        return this;
    }

    public CommentBuilder setCommentContent(String commentContent) {
        this.commentContent = commentContent;
        return this;
    }

    public CommentBuilder setCommentAuthor(String commentAuthor) {
        this.commentAuthor = commentAuthor;
        return this;
    }

    public Comment createComment() {
        return new Comment(commentID, commentDate, commentContent, commentAuthor);
    }
}