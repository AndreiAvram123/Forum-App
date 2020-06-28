package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Post
import com.example.dataLayer.models.PostDTO

fun PostDTO.toPost(): Post {
    val user = UserMapper.mapToDomainObject(this.author)

    return Post(id = this.id,
            title = this.title,
            image = this.image,
            date = this.date,
            authorID = user.userID,
            content = this.content

    ).apply { author = user }
}

object PostMapper {


    fun mapToDomainObject(postDTO: PostDTO): Post {

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