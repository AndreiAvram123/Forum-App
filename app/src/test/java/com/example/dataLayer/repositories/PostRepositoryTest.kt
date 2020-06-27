package com.example.dataLayer.repositories

import android.os.Build
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.TestUtilities
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.dataLayer.LocalDatabase
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.dao.RoomPostDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject


@InternalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1], application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
class PostRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    private lateinit var postDao: RoomPostDao

    @Inject
    private val repo: PostRepositoryInterface
            = TestUtilities.retrofit.create(PostRepositoryInterface::class.java)

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
