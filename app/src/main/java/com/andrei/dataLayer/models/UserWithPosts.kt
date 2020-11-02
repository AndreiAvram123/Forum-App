package com.andrei.dataLayer.models

import androidx.room.Embedded
import androidx.room.Relation
import com.andrei.kit.models.Post
import com.andrei.kit.models.User

data class UserWithPosts(
        @Embedded val user: User,
        @Relation(
                parentColumn = "userID",
                entityColumn = "authorID"
        )
        val posts: List<Post>
)