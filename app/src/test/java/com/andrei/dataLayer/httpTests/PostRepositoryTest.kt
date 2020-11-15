package com.andrei.dataLayer.httpTests

import android.os.Build
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.andrei.TestUtilities
import com.andrei.TestUtilities.Companion.testPost
import com.andrei.TestUtilities.Companion.testPostID
import com.andrei.TestUtilities.Companion.testUser
import com.andrei.TestUtilities.Companion.testUserID
import com.andrei.dataLayer.LocalDatabase
import com.andrei.dataLayer.interfaces.PostRepositoryInterface
import com.andrei.dataLayer.interfaces.dao.RoomPostDao
import com.andrei.dataLayer.models.serialization.SerializeFavoritePostRequest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class PostRepositoryTest {


    private val repo: PostRepositoryInterface =
            TestUtilities.retrofit.create(PostRepositoryInterface::class.java)


    private lateinit var postDao: RoomPostDao

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().targetContext,
                LocalDatabase::class.java
        ).build()
        postDao = db.postDao()
        runBlocking {
            db.userDao().insertUser(testUser)
            db.postDao().insertPost(testPost)
        }
    }


    @Test
    fun shouldReturnPosts() {
        runBlocking {
            repo.fetchNextPagePosts(TestUtilities.lastPostIDDB)
        }
    }

    @Test
    fun shouldReturnPost() = runBlocking {
        val subject = repo.fetchPostByID(testPostID)
        Assert.assertNotNull(subject)
    }

    @Test
    fun shouldFetchRecentPosts() = runBlocking {
        val fetchedData = repo.fetchRecentPosts()
        assert(fetchedData.isNotEmpty())
    }


    @Test
    fun shouldReturnUserFavoritePost() = runBlocking {
        val favoritePosts = repo.fetchUserFavoritePosts(testUser.userID)
        Assert.assertNotNull(favoritePosts)
    }




    @Test
    fun shouldReturnNotNullFavoritePosts() = runBlocking {
        val fetchedFavoritePosts = repo.fetchUserFavoritePosts(testUserID)
        Assert.assertNotNull(fetchedFavoritePosts)
    }



    @Test
    fun `add post to favorites given post and user ID` ()= runBlocking{
        val favoritePostRequest = SerializeFavoritePostRequest(postID = testPostID,userID = testUserID)
        val response = repo.addPostToFavorites(favoritePostRequest)
        Assert.assertTrue(response.successful)
    }


    @Test
    fun shouldFavoritePostsNotIncludeRemovedPost() {
        runBlocking {
            //add a post to favorites that we know it exists
            val favoritePostRequest = SerializeFavoritePostRequest(postID = testPostID,userID = testUserID)
           repo.addPostToFavorites(favoritePostRequest)
            val serverResponse = repo.removePostFromFavorites(userID = favoritePostRequest.userID, postID = favoritePostRequest.postID)
            Assert.assertTrue(serverResponse.successful)
        }
    }
    @Test
    fun shouldReturnNotNulUserPosts() = runBlocking {
        val fetchedPosts = repo.fetchMyPosts(testUserID)
        Assert.assertNotNull(fetchedPosts)
    }
}

