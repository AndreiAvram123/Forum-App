package com.example.bookapp.fragments

import android.os.Build
import com.example.bookapp.AppUtilities
import com.example.bookapp.activities.WelcomeActivity
import com.example.bookapp.models.User
import com.example.dataLayer.interfaces.PostRepositoryInterface
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@InternalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class FragmentUserPostsTest {
    private val postRepo = AppUtilities.getRetrofit().create(PostRepositoryInterface::class.java)
    private val userID = 109

    @Test
    fun shouldReturnNotNulUserPosts() = runBlocking {
        val fetchedPosts = postRepo.fetchMyPosts(userID)
        assertNotNull(fetchedPosts)
    }
}