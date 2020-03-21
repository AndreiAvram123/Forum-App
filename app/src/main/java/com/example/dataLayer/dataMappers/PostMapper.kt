package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Post
import com.example.dataLayer.models.PostDTO

object PostMapper {

    /**
     * This method is used to convert the dto network objects to
     * domain specific objects
     */
    fun convertDTONetworkToDomainObjects(dboPosts: ArrayList<PostDTO>): ArrayList<Post> {
        val toReturn: ArrayList<Post> = ArrayList();
        dboPosts.forEach {
            toReturn.add(convertDtoObjectToDomainObject(it));
        }
        return toReturn
    }

    fun convertDtoObjectToDomainObject(postDTO: PostDTO?): Post {
          postDTO?.let{
              val builder: Post.Builder = Post.Builder();
              builder.postID = postDTO.postID;
              builder.postTitle = postDTO.postTitle
              builder.postImage = postDTO.postImage
              builder.postDate = postDTO.postDate
              builder.postAuthor = postDTO.postAuthor
              builder.postCategory = postDTO.postCategory
              builder.postContent = postDTO.postContent
              return builder.build()
          }
        return Post.buildNullSafeObject()

    }
}