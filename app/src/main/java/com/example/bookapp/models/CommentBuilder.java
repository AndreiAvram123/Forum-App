package com.example.bookapp.models;

//todo
//needs some refactoring
public class CommentBuilder {
    private int commentID;
    private String commentUserID;
    private String commentDate;
    private String commentContent;
    private String commentAuthor;
    private int postID;

    public CommentBuilder setCommentUserID(String commentUserID) {
        this.commentUserID = commentUserID;
        return this;
    }

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
    public CommentBuilder setPostID(int postID) {
        this.postID = postID;
        return this;
    }

    public Comment createComment() {
        if (commentID == 0) {
            throw new IllegalStateException("Comment id cannot be null");
        }
        if (commentDate == null) {
            throw new IllegalStateException("Comment date cannot be null");
        }
        if (commentContent == null) {
            throw new IllegalStateException("Comment content cannot be null");
        }


        return new Comment(commentID, commentDate, commentContent, commentAuthor, postID, commentUserID);
    }
}