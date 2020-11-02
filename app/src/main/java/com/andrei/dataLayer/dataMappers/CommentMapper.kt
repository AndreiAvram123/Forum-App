package com.andrew.dataLayer.dataMappers

import com.andrew.bookapp.models.Comment
import com.andrew.dataLayer.models.serialization.CommentDTO

object CommentMapper {


    fun mapToDomainObject(commentDTO: CommentDTO) = Comment(
            id = commentDTO.id,
            date = commentDTO.date,
            content = commentDTO.content,
            user = UserMapper.mapToDomainObject(commentDTO.user),
            postID = commentDTO.postID

    )
}