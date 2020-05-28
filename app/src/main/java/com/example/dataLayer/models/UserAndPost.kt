package com.example.dataLayer.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bookapp.models.Post
import com.example.bookapp.models.User

data class UserAndPost(
        @Embedded val user: User,
        @Relation(
                parentColumn = "userID",
                entityColumn = "postID"
        )
        val post: Post

)