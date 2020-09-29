package com.andrew.dataLayer.models

import androidx.room.Embedded
import androidx.room.Relation
import com.andrew.bookapp.models.Post
import com.andrew.bookapp.models.User

data class UserAndPost(
        @Embedded val user: User,
        @Relation(
                parentColumn = "userID",
                entityColumn = "postID"
        )
        val post: Post

)