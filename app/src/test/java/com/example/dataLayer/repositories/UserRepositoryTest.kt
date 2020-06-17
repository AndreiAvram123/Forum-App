package com.example.dataLayer.repositories

import com.example.TestUtilities
import com.example.dataLayer.interfaces.UserRepositoryInterface
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
    fun shouldReturnUserOnSuccessfulLogin() = runBlocking {
        val username = "lalaband"
        val password = "lalala"
        val user = repo.login(username,password)
        assertNotNull(user)

    }
}