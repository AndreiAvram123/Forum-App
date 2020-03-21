package com.example.dataLayer.interfaces;

import com.example.dataLayer.models.PostDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PostRepositoryInterface {
    @GET("RestfulRequestHandler.php")
    Call<ArrayList<PostDTO>> fetchPosts(@Query("recentPosts") boolean recentPosts);


    @GET("RestfulRequestHandler.php")
    Call<ArrayList<PostDTO>> fetchNewPosts(@Query("recentPosts") boolean recentPosts, @Query("lastPostID") long lastPostID);

    @GET("RestfulRequestHandler.php")
    Call<PostDTO> fetchPostByID(@Query("postID") long postID);

    @GET("RestfulRequestHandler.php")
    Call<ArrayList<PostDTO>> fetchFavoritePostsByUserID(@Query("userID") String userID, @Query("savedPosts") boolean favoritePosts);

    @GET("RestfulRequestHandler.php")
    Call<ArrayList<PostDTO>> fetchMyPosts(@Query("userID") String userID, @Query("myPosts") boolean favoritePosts);


    @POST("RestfulRequestHandler.php")
    Call<PostDTO> uploadPost(@Query("uploadPost") boolean uploadPost, @Body PostDTO serializePost);


    @FormUrlEncoded
    @POST("RestfulRequestHandler.php")
    Call<String> addPostToFavorites(@Query("addPostToFavorite") boolean addToFavorites, @Field("postID") long postID, @Field("userID") String userID);

    @FormUrlEncoded
    @POST("RestfulRequestHandler.php")
    Call<PostDTO> removePostsFromFavorites(@Query("addPostToFavorite") boolean addToFavorites, @Field("postID") int postID, @Field("userID") String userID);


}
