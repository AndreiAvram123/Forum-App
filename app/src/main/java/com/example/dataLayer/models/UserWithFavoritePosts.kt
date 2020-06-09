package com.example.dataLayer.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.bookapp.models.Post
import com.example.bookapp.models.User

data class UserWithFavoritePosts(
        @Embedded val user: User,
        @Relation(
                parentColumn = "userID",
                entityColumn = "postID",
                associateBy = Junction(UserWithFavoritePostsCrossRef::class)
        )
        val posts: List<Post>
)