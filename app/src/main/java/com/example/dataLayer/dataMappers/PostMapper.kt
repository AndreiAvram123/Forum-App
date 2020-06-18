package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Post
import com.example.dataLayer.models.PostDTO

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