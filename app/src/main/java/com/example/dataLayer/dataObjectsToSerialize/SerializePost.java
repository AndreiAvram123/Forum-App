package com.example.dataLayer.dataObjectsToSerialize;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SerializePost {

    @SerializedName("postTitle")
    @Expose
    private String postTitle;
    @SerializedName("postContent")
    @Expose
    private String postContent;
    @SerializedName("postCategory")
    @Expose
    private String postCategory;
    @SerializedName("filename")
    @Expose
    private String imageName;
    @SerializedName("image")
    @Expose
    private String imageBase64;
    @SerializedName("postAuthorID")
    @Expose
    private String postAuthorID;


    public static class Builder {
        private String postTitle;
        private String postContent;
        private String postCategory;
        private String imageName;
        private String imageBase64;

        public Builder setPostTitle(String postTitle) {
            this.postTitle = postTitle;
            return this;
        }

        public Builder setPostContent(String postContent) {
            this.postContent = postContent;
            return this;
        }

        public Builder setPostCategory(String postCategory) {
            this.postCategory = postCategory;
            return this;
        }

        public Builder setImageName(String imageName) {
            this.imageName = imageName;
            return this;
        }

        public Builder setImageBase64(String imageBase64) {
            this.imageBase64 = imageBase64;
            return this;
        }

        public SerializePost build() {
            SerializePost serializePost = new SerializePost();
            serializePost.postTitle = postTitle;
            serializePost.postContent = postContent;
            serializePost.postCategory = postCategory;
            serializePost.imageName = imageName;
            serializePost.imageBase64 = imageBase64;
            return serializePost;
        }
    }

}
