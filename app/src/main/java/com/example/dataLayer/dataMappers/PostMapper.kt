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
