package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Post
import com.example.dataLayer.models.PostDTO

object PostMapper {

    /**
     * This method is used to convert the dto network objects to
     * domain specific objects
     */
    fun mapDTONetworkToDomainObjects(dboPosts: ArrayList<PostDTO>) = dboPosts.map { mapDtoObjectToDomainObject(it) }


    fun mapDtoObjectToDomainObject(postDTO: PostDTO?): Post {
        postDTO?.let {
            val user = UserMapper.mapNetworkToDomainObject(postDTO.author)

            val post = Post(id = postDTO.id,
                    title = postDTO.title,
                    image = postDTO.image,
                    date = postDTO.date,
                    authorID = user.userID,
                    content = postDTO.content

            )
            post.author = user
            return post;
        }

        return Post.buildTestPost()

    }

}