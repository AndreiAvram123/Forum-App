package com.andrew.dataLayer.repositories

import com.andrew.TestUtilities
import com.andrew.dataLayer.interfaces.UserRepositoryInterface
import com.andrew.dataLayer.models.UserDTO
import kotlinx.coroutines.runBlocking
import org.junit.Assert
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
    @Test
    fun shouldRegisterWithFirebaseUID() = runBlocking {
        val response = repo.register(TestUtilities.userToRegister)
        assertNotNull(response.token)
    }
}