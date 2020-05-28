package com.example.dataLayer.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bookapp.models.Comment
import com.example.bookapp.models.Post

data class PostWithComments(
        @Embedded
        val post: Post,
        @Relation(
                parentColumn = "postID",
                entityColumn = "commentPostID"
        )
        val comments: List<Comment>

)