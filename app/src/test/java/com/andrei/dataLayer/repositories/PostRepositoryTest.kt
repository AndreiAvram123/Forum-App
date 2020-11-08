package com.andrei.dataLayer.repositories

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.andrei.TestUtilities
import com.andrei.TestUtilities.Companion.testPost
import com.andrei.TestUtilities.Companion.testPostID
import com.andrei.TestUtilities.Companion.testUser
import com.andrei.TestUtilities.Companion.testUserID
import com.andrei.kit.R
import com.andrei.kit.utils.toBase64
import com.andrei.dataLayer.LocalDatabase
import com.andrei.dataLayer.interfaces.PostRepositoryInterface
import com.andrei.dataLayer.interfaces.dao.RoomPostDao
import com.andrei.dataLayer.models.UserWithFavoritePostsCrossRef
import com.andrei.dataLayer.models.serialization.SerializePost
import com.andrei.dataLayer.models.serialization.SerializeFavoritePostRequest
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@InternalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class PostRepositoryTest {


    private val repo: PostRepositoryInterface =
            TestUtilities.retrofit.create(PostRepositoryInterface::class.java)


    private lateinit var postDao: RoomPostDao
    private val context = ApplicationProvider.getApplicationContext<Context>()

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
    fun shouldReturn20Posts() =
            runBlocking {
                val subject = repo.fetchNextPagePosts(TestUtilities.lastPostIDDB)
                Assert.assertEquals(20, subject.size)
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
    fun shouldDeletedFavoritePostNotBeReturnedLocalDB() {
        runBlocking {
            //insert a favorite post
            val postToInsert = UserWithFavoritePostsCrossRef(testPost.id, userID = testUser.userID)
            postDao.addFavoritePost(postToInsert)
            //  retrieve all the user's favorite posts and check whether the post belongs there
            val fetchedPosts = postDao.getFavoritePostsTest(testUser.userID)
            Assert.assertNotEquals(fetchedPosts.posts.size, 0)
        }

    }

    @Test
    fun `given post should upload to server`() {
        runBlocking {
            val drawable = context.applicationContext.getDrawable(R.drawable.placeholder)
            drawable?.let {
                val uploadPost = SerializePost(
                        title = "Placeholder title",
                        content = "Placeholder content",
                        imageData = it.toBase64(),
                        userID = testUserID

                )
                try{
                    val postResponse = repo.uploadPost(uploadPost)
                    Assert.assertNotNull(postResponse)
                }catch (e:Exception){
                    fail()
                }
            }


        }
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

