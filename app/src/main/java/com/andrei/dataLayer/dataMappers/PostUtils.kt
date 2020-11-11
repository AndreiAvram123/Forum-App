package com.andrei.dataLayer.dataMappers

import com.andrei.kit.models.Post
import com.andrei.dataLayer.models.PostDTO

    fun PostDTO.toPost():Post {

       return  Post(id = this.id,
                title = this.title,
                images = this.images.joinToString (),
                date = this.date,
                user = this.author.toUser(),
                content = this.content,
               bookmarkTimes = this.bookmarkTimes,
               numberOfComments =  this.numberOfComments
       )

    }
