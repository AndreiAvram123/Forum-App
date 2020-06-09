package com.example.dataLayer.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bookapp.models.Post
import com.example.bookapp.models.User

data class UserWithPost(
        @Embedded
        val user: User,
        @Relation(
                parentColumn = "userID",
                entityColumn = "authorID"
        )
        val post: Post
)