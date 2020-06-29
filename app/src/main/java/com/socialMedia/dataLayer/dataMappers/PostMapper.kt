package com.socialMedia.dataLayer.dataMappers

import com.socialMedia.bookapp.models.Post
import com.socialMedia.dataLayer.models.PostDTO

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
