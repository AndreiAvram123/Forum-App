package com.example.dataLayer.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CommentDTO(@field:Expose
                      @field:SerializedName("commentPostID")
                      private val commentPostID: Long,
                      @field:Expose @field:SerializedName("commentContent")
                      private val commentContent: String, @field:Expose @field:SerializedName("commentUserID") private val commentUserID: String)