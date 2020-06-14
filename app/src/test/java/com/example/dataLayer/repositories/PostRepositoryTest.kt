package com.example.dataLayer.repositories

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.dao.RoomPostDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.*
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


    private lateinit var postDao: RoomPostDao

    private val user = User(userID = 109, username = "andrei", email = "andrei@yahoo.com", profilePicture = "sdfs")
    private val testPost = Post.buildTestPost()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        //use a cache version of the database
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
    fun shouldReturnNotNullCachedPosts() {
        val liveData = postDao.getCachedPosts()
        Assert.assertNotNull(liveData.getOrAwaitValue())
    }

    fun <T> LiveData<T>.getOrAwaitValue(
            time: Long = 10,
            timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}
