package com.example.dataLayer.dataMappers

import com.example.bookapp.models.Post
import com.example.dataLayer.models.PostDTO
import com.example.dataLayer.models.RoomPostDTO

object PostMapper {

    /**
     * This method is used to convert the dto network objects to
     * domain specific objects
     */
    fun mapDTONetworkToDomainObjects(dboPosts: ArrayList<PostDTO>): ArrayList<Post> {
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

    fun mapRoomDTOToDomainObject(roomPostDTO: RoomPostDTO?): Post {
        roomPostDTO?.let {
            val builder: Post.Builder = Post.Builder();
            builder.postID = roomPostDTO.postID;
            builder.postTitle = roomPostDTO.postTitle
            builder.postImage = roomPostDTO.postImage
            builder.postDate = roomPostDTO.postDate
            builder.postAuthor = roomPostDTO.postAuthor
            builder.postCategory = roomPostDTO.postCategory
            builder.postContent = roomPostDTO.postContent
            return builder.build()
        }
        return Post.buildNullSafeObject()
    }

    fun mapRoomDTOToDomainObjects(posts: List<RoomPostDTO>): ArrayList<Post> {
        val toReturn: ArrayList<Post> = ArrayList()
        posts.forEach {
            toReturn.add(mapRoomDTOToDomainObject(it))

        }
        return toReturn

    }

    fun mapDomainToRoomDTO(fetchedPost: Post): RoomPostDTO {
        return RoomPostDTO(postID = fetchedPost.postID,
                postTitle = fetchedPost.postTitle,
                postImage = fetchedPost.postImage,
                postDate = fetchedPost.postDate,
                postAuthor = fetchedPost.postAuthor,
                postCategory = fetchedPost.postCategory,
                postContent = fetchedPost.postContent)
    }

     fun mapNetworkDTOToRoomDTO(post: PostDTO): RoomPostDTO = RoomPostDTO(postID = post.postID, postTitle = post.postTitle,
            postImage = post.postImage, postDate = post.postDate, postAuthor = post.postAuthor, postCategory = post.postCategory, postContent = post.postContent)

    fun mapNetworkDTOToRoomDTOS(posts: ArrayList<PostDTO>): ArrayList<RoomPostDTO> {
        val toReturn: ArrayList<RoomPostDTO> = ArrayList()
        posts.forEach {
            toReturn.add(mapNetworkDTOToRoomDTO(it))
        }
        return toReturn


    }
}