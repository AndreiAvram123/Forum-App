package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Comment
import com.example.dataLayer.models.serialization.CommentDTO

object CommentMapper {


    fun mapToDomainObject(commentDTO: CommentDTO) = Comment(
            id = commentDTO.id,
            date = commentDTO.date,
            content = commentDTO.content,
            user = UserMapper.mapToDomainObject(commentDTO.user),
            postID = commentDTO.postID

    )
}