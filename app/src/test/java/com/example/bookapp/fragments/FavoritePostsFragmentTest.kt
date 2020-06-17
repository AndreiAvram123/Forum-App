package com.example.bookapp.fragments

import com.example.TestUtilities
import com.example.dataLayer.interfaces.PostRepositoryInterface
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test


@InternalCoroutinesApi
class FavoritePostsFragmentTest {
    private val postRepo = TestUtilities.retrofit.create(PostRepositoryInterface::class.java)
    private val userID = 109


    @Test
    fun shouldReturnNotNullFavoritePosts() = runBlocking {
        val fetchedFavoritePosts = postRepo.fetchUserFavoritePosts(userID)
        Assert.assertNotNull(fetchedFavoritePosts)
    }


    @Test
    fun shouldPostBeAddedToFavorites() {
        runBlocking {
            //add a post to favorites that we know it exists
            val postID = 2236
            postRepo.addPostToFavorites(postID, userID)
            //fetch the users favorite posts and check if this post belongs in the collection
            val favoritePosts = postRepo.fetchUserFavoritePosts(userID)
            favoritePosts.find { it.id == postID }.also {
                Assert.assertNotNull(it)
            }

            //remove it from favorites after the test
            postRepo.removePostFromFavorites(userID = userID, postID = postID)
        }
    }


    @Test
    fun shouldFavoritePostsNotIncludeRemovedPost() {
        runBlocking {
            //add a post to favorites that we know it exists
            val postID = 2236
            postRepo.addPostToFavorites(postID, userID)
            //remove the post
            val serverResponse = postRepo.removePostFromFavorites(postID = postID, userID = userID)
            Assert.assertTrue(serverResponse.successful)
        }
    }
}

