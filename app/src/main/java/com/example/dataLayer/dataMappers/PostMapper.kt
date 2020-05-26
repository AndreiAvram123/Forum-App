package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Post
import com.example.dataLayer.models.PostDTO

object PostMapper {

    /**
     * This method is used to convert the dto network objects to
     * domain specific objects
     */
    fun mapDTONetworkToDomainObjects(dboPosts: ArrayList<PostDTO>): ArrayList<Post> {
        val toReturn: ArrayList<Post> = ArrayList();
        dboPosts.forEach {
            toReturn.add(mapDtoObjectToDomainObject(it));
        }
        return toReturn
    }

    fun mapDtoObjectToDomainObject(postDTO: PostDTO?): Post {
        postDTO?.let {
            val post: Post = Post(postID = postDTO.id,
                    postTitle = postDTO.title,
                    postImage = "",
                    postDate = postDTO.date,
                    authorID = UserMapper.mapNetworkToDomainObject(postDTO.author).userID,
                    postContent = postDTO.content

            );
            return post;
        }

        return Post.buildNullSafeObject()

    }

}