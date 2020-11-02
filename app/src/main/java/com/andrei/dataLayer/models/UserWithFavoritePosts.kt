package com.andrei.dataLayer.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.andrei.kit.models.Post
import com.andrei.kit.models.User

data class UserWithFavoritePosts(
        @Embedded val user: User,
        @Relation(
                parentColumn = "userID",
                entityColumn = "postID",
                associateBy = Junction(UserWithFavoritePostsCrossRef::class)
        )
        val posts: List<Post>
)