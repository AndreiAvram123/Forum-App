package com.example.dataLayer.repositories

import android.os.Build
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.interfaces.dao.RoomPostDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


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


}
