package com.andrew.bookapp.fragments

import android.os.Build
import com.andrew.TestUtilities
import com.andrew.dataLayer.interfaces.PostRepositoryInterface
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.robolectric.annotation.Config

@InternalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class FragmentUserPostsTest {
    private val postRepo = TestUtilities.retrofit.create(PostRepositoryInterface::class.java)
    private val userID = 109

    @Test
    fun shouldReturnNotNulUserPosts() = runBlocking {
        val fetchedPosts = postRepo.fetchMyPosts(userID)
        assertNotNull(fetchedPosts)
    }
}