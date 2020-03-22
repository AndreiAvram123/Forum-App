package com.example.dataLayer.dataObjectsToSerialize;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SerializeComment {
    @SerializedName("commentPostID")
    @Expose
    private long commentPostID;

    @SerializedName("commentContent")
    @Expose
    private String commentContent;

    @SerializedName("commentUserID")
    @Expose
    private String commentUserID;


    public SerializeComment(long commentPostID, String commentContent, String commentUserID) {
        this.commentPostID = commentPostID;
        this.commentContent = commentContent;
        this.commentUserID = commentUserID;
    }
}
