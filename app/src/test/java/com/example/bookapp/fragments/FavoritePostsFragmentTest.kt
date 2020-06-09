package com.example.bookapp.fragments

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.example.bookapp.AppUtilities
import com.example.bookapp.activities.WelcomeActivity
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.dao.RoomPostDao
import com.example.dataLayer.models.UserWithFavoritePostsCrossRef
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.Executors


@InternalCoroutinesApi
class FavoritePostsFragmentTest {
    private val postRepo = AppUtilities.getRetrofit().create(PostRepositoryInterface::class.java)
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

