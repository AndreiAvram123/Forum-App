package com.andrew.bookapp.fragments

import com.andrew.TestUtilities
import com.andrew.dataLayer.interfaces.PostRepositoryInterface
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
    fun shouldFavoritePostsNotIncludeRemovedPost() {
        runBlocking {
            //add a post to favorites that we know it exists
            val postID = 1108
            postRepo.addPostToFavorites(postID, userID)
            //remove the post
            val serverResponse = postRepo.removePostFromFavorites(postID = postID, userID = userID)
            Assert.assertTrue(serverResponse.successful)
        }
    }
}

