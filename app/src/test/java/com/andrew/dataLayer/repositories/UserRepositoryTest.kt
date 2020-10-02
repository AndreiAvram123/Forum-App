package com.andrew.dataLayer.repositories

import com.andrew.TestUtilities
import com.andrew.dataLayer.interfaces.UserRepositoryInterface
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
    fun shouldReturnNotNullSearchSuggestions()= runBlocking {
        val query = "av"
        val fetchedData = repo.fetchSuggestions(query)
        assertNotNull(fetchedData)
    }
}