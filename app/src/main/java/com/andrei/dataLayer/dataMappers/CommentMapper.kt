package com.andrei.dataLayer.dataMappers

import com.andrei.kit.models.Comment
import com.andrei.dataLayer.models.serialization.CommentDTO

object CommentMapper {


    fun mapToDomainObject(commentDTO: CommentDTO) = Comment(
            id = commentDTO.id,
            date = commentDTO.date,
            content = commentDTO.content,
            user = UserMapper.mapToDomainObject(commentDTO.user),
            postID = commentDTO.postID

    )
}