package com.andrei.dataLayer.repositories


 enum class Endpoint (val url:String){

     RECENT_POSTS("/api/recentPosts"),

     PUSH_MESSAGE("/api/messages/push"),
     LAST_CHAT_MESSAGES("/api/user/{userID}/chats/lastMessages"),
     CHAT_LINK("/api/chats/discover/{userID}"),
     USERS_CHAT("/api/user/{userID}/chats")
 }