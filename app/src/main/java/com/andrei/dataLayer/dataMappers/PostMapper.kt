package com.andrew.dataLayer.dataMappers

import com.andrew.bookapp.models.Post
import com.andrew.dataLayer.models.PostDTO


    fun PostDTO.toPost():Post {
      val user = this.author.toUser()
       return  Post(id = this.id,
                title = this.title,
                image = this.image,
                date = this.date,
                authorID = user.userID,
                content = this.content

        ).apply { author = user }

    }
