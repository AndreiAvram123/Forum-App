package com.socialMedia.dataLayer.repositories

import android.os.Build
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.socialMedia.TestUtilities
import com.socialMedia.bookapp.models.Post
import com.socialMedia.bookapp.models.User
import com.socialMedia.dataLayer.LocalDatabase
import com.socialMedia.dataLayer.interfaces.dao.RoomPostDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@InternalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)


class PostRepositoryTest {


    private lateinit var postDao: RoomPostDao

    private val repo: PostRepositoryInterface = TestUtilities.retrofit.create(PostRepositoryInterface::class.java)

    private val user = User(userID = 109, username = "andrei", email = "andrei@yahoo.com", profilePicture = "sdfs")
    private val testPost = Post.buildTestPost()


    @Before
    fun setUp() {
        //use a cache version of the database
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
    fun shouldFetchRecentPosts() = runBlocking {
        val fetchedData = repo.fetchRecentPosts()
        assert(!fetchedData.isNullOrEmpty())
    }


}
