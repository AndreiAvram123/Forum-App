package com.example.bookapp

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.bookapp.activities.WelcomeActivity
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.dao.RoomPostDao
import com.example.dataLayer.models.UserWithFavoritePostsCrossRef
import junit.framework.Assert.fail
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class PostRepositoryTest {


    private val userRepoInterface: PostRepositoryInterface =
            AppUtilities.getRetrofit().create(PostRepositoryInterface::class.java)


    private lateinit var postDao: RoomPostDao

    private val user = User(userID = 109, username = "pupu", email = "caca@yahoo.com", profilePicture = "sdfs")
    private val testPost = Post.buildTestPost()

    @Before
    @Throws(Exception::class)
    fun setUp() {

        val db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().targetContext,
                PostDatabase::class.java
        ).build()
        postDao = db.postDao()
        runBlocking {
            db.userDao().insertUser(user)
            db.postDao().insertPost(testPost)
        }

    }


    @Test
    fun shouldReturn20Posts() =
            runBlocking {
                val subject = userRepoInterface.fetchNextPage(1)
                Assert.assertEquals(20, subject.size)
            }

    @Test
    fun shouldReturnPost() = runBlocking {
        val subject = userRepoInterface.fetchPostByID(2236)
        Assert.assertNotNull(subject)
    }

    @Test
    fun shouldReturnUserFavoritePost() = runBlocking {
        val favoritePosts = userRepoInterface.fetchUserFavoritePosts(109)
        Assert.assertNotNull(favoritePosts)

    }

    @Test
    fun shouldDeletedFavoritePostNotBeReturnedLocalDB() {
        runBlocking {
            //insert a favorite post
            val postToInsert = UserWithFavoritePostsCrossRef(testPost.id, userID = user.userID)
            postDao.addFavoritePost(postToInsert)
            //  retrieve all the user's favorite posts and check whether the post belongs there
            val fetchedPosts = postDao.getFavoritePostsTest(user.userID)
            Assert.assertNotEquals(fetchedPosts.posts.size, 0)
        }

    }
}

