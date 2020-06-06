package com.example.bookapp

import com.example.bookapp.models.User
import com.example.dataLayer.interfaces.PostRepositoryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class SimpleTests {


    private val userRepoInterface: PostRepositoryInterface =
            AppUtilities.getRetrofit().create(PostRepositoryInterface::class.java)


    private val user = User(userID = 109, username = "", email = "", profilePicture = "")


    @Test
    fun shouldReturn20Posts() =
            runBlocking {
                val subject = userRepoInterface.fetchNextPage(1)
                Assert.assertEquals(20, subject.size)
            }

    @Test
    fun shouldReturnPost() = runBlocking {
        val subject = userRepoInterface.fetchPostByID(1144)
        Assert.assertNotNull(subject)
    }

    @Test
    fun shouldReturnUserFavoritePost() = runBlocking {
        val favoritePosts = userRepoInterface.fetchFavoritePostsByUserID(109)
        Assert.assertNotNull(favoritePosts)

    }



}