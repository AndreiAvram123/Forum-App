package com.socialMedia.dataLayer.models

import androidx.room.Embedded
import androidx.room.Relation
import com.socialMedia.bookapp.models.Comment
import com.socialMedia.bookapp.models.Post

data class PostWithComments(
        @Embedded
        val post: Post,
        @Relation(
                parentColumn = "postID",
                entityColumn = "commentPostID"
        )
        val comments: List<Comment>

)