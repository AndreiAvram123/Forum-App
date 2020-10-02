package com.andrew.bookapp.fragments

import com.andrew.TestUtilities
import com.andrew.TestUtilities.Companion.testPostID
import com.andrew.TestUtilities.Companion.testUserID
import com.andrew.dataLayer.interfaces.PostRepositoryInterface
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test


@InternalCoroutinesApi
class FavoritePostsFragmentTest {
    private val postRepo = TestUtilities.retrofit.create(PostRepositoryInterface::class.java)



    @Test
    fun shouldReturnNotNullFavoritePosts() = runBlocking {
        val fetchedFavoritePosts = postRepo.fetchUserFavoritePosts(testUserID)
        Assert.assertNotNull(fetchedFavoritePosts)
    }




    @Test
    fun shouldFavoritePostsNotIncludeRemovedPost() {
        runBlocking {
            //add a post to favorites that we know it exists
            postRepo.addPostToFavorites(testPostID, testUserID)
            //remove the post
            val serverResponse = postRepo.removePostFromFavorites(postID = testPostID, userID = testUserID)
            Assert.assertTrue(serverResponse.successful)
        }
    }
}

