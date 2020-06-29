package com.socialMedia.dataLayer.dataMappers

import com.socialMedia.bookapp.models.Comment
import com.socialMedia.dataLayer.models.serialization.CommentDTO

fun CommentDTO.toComment(id:String = "Unknown") = Comment(id = id,postID = postID,date = date,content = content,user = this.user.toUser())