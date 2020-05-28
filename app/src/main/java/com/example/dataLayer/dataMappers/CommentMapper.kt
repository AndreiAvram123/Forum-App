package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Comment
import com.example.dataLayer.dataObjectsToSerialize.CommentDTO

object CommentMapper {

    fun mapDTOObjectsToDomainObjects(comments: List<CommentDTO>): List<Comment> = comments.map { mapDtoObjectToDomainObject(it) }

    fun mapDtoObjectToDomainObject(commentDTO: CommentDTO) = Comment(
            id = commentDTO.id,
            date = commentDTO.date,
            content = commentDTO.content,
            user = UserMapper.mapNetworkToDomainObject(commentDTO.user),
            postID = commentDTO.postID

    )
}