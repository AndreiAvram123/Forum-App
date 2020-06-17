package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Post
import com.example.dataLayer.models.PostDTO

object PostMapper {

    /**
     * This method is used to convert the dto network objects to
     * domain specific objects
     */
    fun mapToDomainObjects(dboPosts: List<PostDTO>) = dboPosts.map { mapDtoObjectToDomainObject(it) }


    fun mapDtoObjectToDomainObject(postDTO: PostDTO ): Post {

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