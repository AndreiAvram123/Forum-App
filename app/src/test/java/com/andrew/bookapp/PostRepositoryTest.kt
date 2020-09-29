package com.andrew.bookapp

import android.os.Build
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.andrew.TestUtilities
import com.andrew.bookapp.models.Post
import com.andrew.bookapp.models.User
import com.andrew.dataLayer.LocalDatabase
import com.andrew.dataLayer.interfaces.PostRepositoryInterface
import com.andrew.dataLayer.interfaces.dao.RoomPostDao
import com.andrew.dataLayer.models.UserWithFavoritePostsCrossRef
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class PostRepositoryTest {


    private val userRepoInterface: PostRepositoryInterface =
            TestUtilities.retrofit.create(PostRepositoryInterface::class.java)


    private lateinit var postDao: RoomPostDao

    private val user = User(userID = 109, username = "pupu", email = "caca@yahoo.com", profilePicture = "sdfs")
    private val testPost = Post.buildTestPost()

    @Before
    @Throws(Exception::class)
    fun setUp() {

        val db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().targetContext,
                LocalDatabase::class.java
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
                val subject = userRepoInterface.fetchNextPagePosts(100000)
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
    @Test
    fun shouldReturnError(){
        runBlocking {
          val result = userRepoInterface.fetchPostByID(100)
          print(result.toString())
        }
    }
}

