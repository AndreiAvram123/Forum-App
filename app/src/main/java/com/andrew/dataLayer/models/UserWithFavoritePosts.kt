package com.andrew.dataLayer.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.andrew.bookapp.models.Post
import com.andrew.bookapp.models.User

data class UserWithFavoritePosts(
        @Embedded val user: User,
        @Relation(
                parentColumn = "userID",
                entityColumn = "postID",
                associateBy = Junction(UserWithFavoritePostsCrossRef::class)
        )
        val posts: List<Post>
)