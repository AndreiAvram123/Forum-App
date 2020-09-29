package com.andrew.dataLayer.dataMappers

import com.andrew.bookapp.models.Post
import com.andrew.dataLayer.models.PostDTO

object PostMapper {


    fun mapToDomainObject(postDTO: PostDTO ): Post {

            val user = UserMapper.mapToDomainObject(postDTO.author)

            return Post(id = postDTO.id,
                    title = postDTO.title,
                    image = postDTO.image,
                    date = postDTO.date,
                    authorID = user.userID,
                    content = postDTO.content

            ).apply { author = user }

    }

}