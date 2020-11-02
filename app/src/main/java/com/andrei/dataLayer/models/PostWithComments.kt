package com.andrei.dataLayer.models

import androidx.room.Embedded
import androidx.room.Relation
import com.andrei.bookapp.models.Comment
import com.andrei.bookapp.models.Post

data class PostWithComments(
        @Embedded
        val post: Post,
        @Relation(
                parentColumn = "postID",
                entityColumn = "commentPostID"
        )
        val comments: List<Comment>

)