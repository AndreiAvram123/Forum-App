package com.andrei.dataLayer.dataMappers

import com.andrei.kit.models.Comment
import com.andrei.dataLayer.models.serialization.CommentDTO

fun CommentDTO.toComment()
        = Comment(
        id = this.id,
        date = this.date,
        content = this.content,
        user = this.user.toUser(),
        postID = this.postID

)
