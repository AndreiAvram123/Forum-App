package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Post
import com.example.dataLayer.models.PostDTO

fun PostDTO.toPost(id: String = "unknown"): Post {
    val user = this.author.toUser()

    return Post(id = id,
            title = this.title,
            image = this.image,
            date = this.date,
            authorID = user.userID,
            content = this.content

    ).apply { author = user }
}
