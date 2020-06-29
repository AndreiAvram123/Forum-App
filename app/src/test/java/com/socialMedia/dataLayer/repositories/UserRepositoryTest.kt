package com.socialMedia.dataLayer.repositories

import com.socialMedia.TestUtilities
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {
    private lateinit var repo: UserRepositoryInterface

    @Before
    fun initialize() {
        repo = TestUtilities.retrofit.create(UserRepositoryInterface::class.java)

    }

    @Test
    fun shouldLoginWithGoogleToken() = runBlocking {
        val token = "104971103964321040701"
        val response = repo.fetchGoogleUser(token)
        assert(response.userDTO !=null)

    }
    @Test
    fun shouldReturnNotNullSearchSuggestions()= runBlocking {
        val query = "av"
        val fetchedData = repo.fetchSuggestions(query)
        assertNotNull(fetchedData)
    }
}