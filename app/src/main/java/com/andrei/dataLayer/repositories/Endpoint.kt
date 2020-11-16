package com.andrei.dataLayer.repositories


 enum class Endpoint (val url:String){

     /**
      * POSTS
      */
     USER_FAVORITE_POSTS("/api/user/{userID}/favoritePosts"),
     RECENT_POSTS("/api/recentPosts"),
     POST_BY_ID("api/post/{id}"),
     POSTS_BY_PAGE("api/posts/page/{lastPostID}"),
     USER_CREATED_POSTS("/api/user/{userID}/posts"),
     REMOVE_BOOKMARKED_POST("api/user/{userID}/removeFromFavorites/{postID}"),
     BOOKMARK_POST("api/posts/addToFavorites"),
     CREATE_POST("/api/posts/create"),



     PUSH_MESSAGE("/api/messages/push"),
     LAST_CHAT_MESSAGES("/api/user/{userID}/chats/lastMessages"),
     CHAT_LINK("/api/chats/discover/{userID}"),
     USERS_CHAT("/api/user/{userID}/chats")
 }