package com.andrew.dataLayer.models

import androidx.room.Embedded
import androidx.room.Relation
import com.andrew.bookapp.models.Comment
import com.andrew.bookapp.models.Post

data class PostWithComments(
        @Embedded
        val post: Post,
        @Relation(
                parentColumn = "postID",
                entityColumn = "commentPostID"
        )
        val comments: List<Comment>

)