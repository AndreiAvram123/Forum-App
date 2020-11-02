package com.andrei.dataLayer.models

import androidx.room.Embedded
import androidx.room.Relation
import com.andrei.kit.models.Comment
import com.andrei.kit.models.Post

data class PostWithComments(
        @Embedded
        val post: Post,
        @Relation(
                parentColumn = "postID",
                entityColumn = "commentPostID"
        )
        val comments: List<Comment>

)