package com.socialMedia.dataLayer.dataMappers

import com.socialMedia.bookapp.models.Comment
import com.socialMedia.dataLayer.models.serialization.CommentDTO

object CommentMapper {


    fun mapToDomainObject(commentDTO: CommentDTO) = Comment(
            id = commentDTO.id,
            date = commentDTO.date,
            content = commentDTO.content,
            user = commentDTO.user.toUser(),
            postID = commentDTO.postID

    )
}