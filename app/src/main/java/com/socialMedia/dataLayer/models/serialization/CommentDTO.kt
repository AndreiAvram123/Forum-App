package com.socialMedia.dataLayer.models.serialization

import com.socialMedia.dataLayer.models.UserDTO
import com.google.gson.annotations.SerializedName
import java.util.*

data class CommentDTO(
        val postID: String = "",
        val date: Long = Calendar.getInstance().timeInMillis / 1000,
        val content: String = "",
        val user: UserDTO = UserDTO()

)