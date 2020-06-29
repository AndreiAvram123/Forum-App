package com.socialMedia.dataLayer.models

import androidx.room.Embedded
import androidx.room.Relation
import com.socialMedia.bookapp.models.Post
import com.socialMedia.bookapp.models.User

data class UserWithPosts(
        @Embedded val user: User,
        @Relation(
                parentColumn = "userID",
                entityColumn = "authorID"
        )
        val posts: List<Post>
)