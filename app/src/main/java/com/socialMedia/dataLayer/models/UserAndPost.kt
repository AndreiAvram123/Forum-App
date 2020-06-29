package com.socialMedia.dataLayer.models

import androidx.room.Embedded
import androidx.room.Relation
import com.socialMedia.bookapp.models.Post
import com.socialMedia.bookapp.models.User

data class UserAndPost(
        @Embedded val user: User,
        @Relation(
                parentColumn = "userID",
                entityColumn = "postID"
        )
        val post: Post

)