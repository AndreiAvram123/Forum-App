package com.example.bookapp.dataLayer.interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PostRepositoryInterface {
    @GET("RestfulRequestHandler.php")
    Call<String> fetchCurrentPosts(@Query("recentPosts") boolean recentPosts);


    @GET("RestfulRequestHandler.php")
    Call<String> fetchCommentsByPostID(@Query("postID") int postID, @Query("comments") boolean fetchComments);

    @GET("RestfulRequestHandler.php")
    Call<String> fetchPostByID(@Query("postID") int postID);

    @GET("RestfulRequestHandler.php")
    Call<String> fetchFavoritePostsByUserID(@Query("userID") String userID, @Query("savedPosts") boolean favoritePosts);

    @GET("RestfulRequestHandler.php")
    Call<String> fetchMyPosts(@Query("userID") String userID, @Query("myPosts") boolean favoritePosts);

    @FormUrlEncoded
    @POST("RestfulRequestHandler.php")
    Call<String> addPostToFavorites(@Query("addPostToFavorite") boolean addToFavorites, @Field("postID") int postID, @Field("userID") String userID);


    @FormUrlEncoded
    @POST("RestfulRequestHandler.php")
    Call<String> removePostsFromFavorites(@Query("addPostToFavorite") boolean addToFavorites, @Field("postID") int postID, @Field("userID") String userID);
}
