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
          postDTO?.let{
              if (!postDTO.postImage.contains("https://i.picsum.photos/")) {
                  postDTO.postImage = "http://sgb967.poseidon.salford.ac.uk/cms/" + postDTO.postImage
              }
              val post = Post(postDTO.postID, postDTO.postTitle, postImage = postDTO.postImage);
              post.postDate = postDTO.postDate;
              post.postAuthor = postDTO.postAuthor;
              post.postCategory = postDTO.postCategory
              post.postContent = postDTO.postContent
              post.isFavorite = postDTO.isFavorite
              post.postAuthorID = postDTO.postAuthorID;
              return post;
          }

        return Post.buildNullSafeObject()

    }

}