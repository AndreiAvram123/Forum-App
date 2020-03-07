package com.example.bookapp.dataLayer.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PostRepositoryInterface {
    @GET("RestfulRequestHandler.php")
    Call<String> fetchCurrentPosts(@Query("recentPosts") boolean recentPosts);


    @GET("RestfulRequestHandler.php")
    Call<String> fetchCommentsByPostID(@Query("postID") int postID, @Query("comments") boolean fetchComments);

    @GET("RestfulRequestHandler.php")
    Call<String> fetchPostByID(@Query("postID") int postID);
}
